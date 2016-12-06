package com.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class PrivateConversation implements MessageQueryable {
    String currentUserId;
    String otherUserId;

    public PrivateConversation(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> privateMessages = new ArrayList<Message>();

        String date = JDBCConnection.convertDate(queryDateParam);
        if(messagesOlderThan) {
            date = "M.tstamp <= " + date + " order by M.mid desc ";
        } else {
            date = "M.tstamp >= " + date + " order by M.mid asc ";
        }
        String query =
                "select M.mid,M.sender,M.data,M.tstamp " +
                        "from Private_Messages PM, Messages M " +
                        "where PM.owner='" + currentUserId + "' and " +
                            " PM.messageid=M.mid and " +
                            " (M.sender='"+currentUserId+"' or M.sender='"+otherUserId+"') and " + date;

        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                String mOwner = result.getString("sender").trim();
                String mData = result.getString("data").trim();
                Message message = new Message(mOwner, mData);
                message.setMessageId(result.getInt("mid"));
                message.setDatePosted(result.getTimestamp("tstamp"));
                privateMessages.add(message);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in circle: " + e.toString());
        }
        return privateMessages;
    }

    public void postMessage(Message message) {
        System.out.println("In Private Conversation Post Message");

        // TODO: post message for the given two users private conversation.
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();

            String q1 = "insert into Private_Messages values " +
                    "(" + message.getMessageId() + ",'" + currentUserId + "')";
            String q2 = "insert into Private_Messages values " +
                    "(" + message.getMessageId() + ",'" + otherUserId + "')";

            statement.executeUpdate(q1);
            statement.executeUpdate(q2);

            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in circle: " + e.toString());
        }


    }
}
