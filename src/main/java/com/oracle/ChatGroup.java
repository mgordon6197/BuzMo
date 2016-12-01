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
        // if messagesOlderThan == true then we want messages OLDER than queryDateParam
        // e.g., if
        ArrayList<Message> messages = new ArrayList<Message>();

        // test data
        messages.add(new Message("messageownerid", "Hello this is a message."));

        // TODO: query most recent messages in this chat in order of oldest first.
        try {
            DateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String sqlstringdate = dateformat.format(queryDateParam);
            String sqlcomparedate = "";
            if(messagesOlderThan) {
                sqlcomparedate = "M.tstamp < '" + sqlstringdate + "' order by desc ";
            } else {
                sqlcomparedate = "M.tstamp > '" + sqlstringdate + "' order by asc ";
            }
            String query =
                    "select " +
                    "from Group_Messages GM, Messages M " +
                    "where GM.group_name = '" + name + "' and " +
                            "GM.messageid = M.mid and " +
                            "date < querydate and " + sqlcomparedate +
                            " limit 7";
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {

            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL exception in queryMessages in chatgroup");
        }



        return messages;
    }

    public void postMessage(Message message) {

        // TODO: store the message in the database.
    }

    public void updateDuration(String newDuration) {

        // TODO: update chatgroup row with newDuration.
    }

    public void updateName(String newName) {

        // TODO: update chatgroup row with newName.
    }

    public void addFriendToChatGroup(User friendToAdd) {

        Request newChatGroupRequest = new Request(friendToAdd, this, RequestType.CHATGROUP_INVITE);

        newChatGroupRequest.createRequest();
    }

    public void createChatGroup(ChatGroup newChatgroup) {
        // TODO: create chatgroup from passed ChatGroup object.
    }

    public String returnId() {
        return name;
    }
}
