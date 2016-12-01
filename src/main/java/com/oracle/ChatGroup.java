package com.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 */
public class ChatGroup implements MessageQueryable, Addable{
    String ownerId;
    String name;
    int duration;

    public ChatGroup(String ownerId, String name, int duration) {
        this.ownerId = ownerId;
        this.name = name;
        this.duration = duration;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String sqlstringdate = dateformat.format(queryDateParam);
            sqlstringdate = "TO_TIMESTAMP('" + sqlstringdate + "','" + "yyyy-mm-dd hh:mi:ss')";
            String sqlcomparedate = "";
            if(messagesOlderThan) {
                sqlcomparedate = "M.tstamp < " + sqlstringdate + " order by M.mid desc ";
            } else {
                sqlcomparedate = "M.tstamp > '" + sqlstringdate + "' order M.mid by asc ";
            }
            String query =
                    "select M.mid,M.sender,M.data " +
                    "from Group_Messages GM, Messages M " +
                    "where GM.group_name = '" + name + "' and " +
                            "GM.messageid = M.mid and " +
                            sqlcomparedate +
                            " ";
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                // add each message to message set
                String mOwner = result.getString("sender").trim();
                String mData = result.getString("data").trim();
                Message message = new Message(mOwner,mData);
                messages.add(message);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL exception in queryMessages in chatgroup: " + e.toString());
        }
        return messages;
    }

    // assume message is less than 1400 characters?
    public void postMessage(Message message) {
        // TODO CHANGE MID
        int mid = 30;
        String date = JDBCConnection.convertDate(new Date());
        String data = "'" + message.getMessage() + "'";
        String sender = "'" + message.getOwnerId() + "'";
        String groupid = "'" + name + "'";

        String query1 = "insert into Messages values ("+mid +","+ data +","+ date +","+ sender+")";
        String query2 = "insert into Group_Messages values ("+groupid +","+ mid +","+ date+")";

        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query1);
            statement.executeUpdate(query2);
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception in postMessage in chatgroup: " + e.toString());
        }
    }

    public void updateDuration(int newDuration) {
        // delete from messages where mid =
        //     select messageid from group_messages g where g.group_name = 'name' and g.since < newduration
        // TODO: update chatgroup row with newDuration.

        String query = "update Group_Owner" +
                " set duration = " + newDuration + " " +
                " where gname = '" + name + "'";
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception in updateDuration: " + e.toString());
        }
    }

    public void updateName(String newName) throws SQLException {

        // insert new one, update, then remove old one
        String insertQuery = "insert into Group_Owner values (" +
                    "'" + newName + "'," + duration + ",'" + ownerId + "')";
        String updateQuery = "update Member_Of " +
                    " set gname= '" + newName + "' " +
                    " where gname = '" + name + "' and userid = '" + ownerId + "'";
        String removeQuery = "delete from Group_Owner" +
                    " where gname = '" + name + "'";

        Connection connection = JDBCConnection.createDBConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(insertQuery);
        statement.execute(updateQuery);
        statement.execute(removeQuery);
        statement.close();

        name = newName;
    }

    public void addFriendToChatGroup(User friendToAdd) {

        Request newChatGroupRequest = new Request(friendToAdd, this, RequestType.CHATGROUP_INVITE);

        newChatGroupRequest.createRequest();
    }

    public void createChatGroup(ChatGroup newChatgroup) throws SQLException {
        String insertQuery = "insert into Group_Owner values (" +
                "'" + newChatgroup.getName() + "'," + newChatgroup.duration + ",'" + newChatgroup.getOwnerId() + "')";
        String insertQuery2 = "insert into Member_Of values (" +
                "'" + newChatgroup.getName() + "','" + newChatgroup.getOwnerId() + "')";

        Connection connection = JDBCConnection.createDBConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(insertQuery);
        statement.executeUpdate(insertQuery2);
        statement.close();
    }

    public String returnId() {
        return name;
    }
}
