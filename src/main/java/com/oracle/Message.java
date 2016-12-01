package com.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class Message {
    private String ownerId;
    private int messageId;
    private String message;
    private Date datePosted;

    public Message(String ownerId, String message) {
        this.ownerId = ownerId;
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return "\nFrom: " + ownerId + "\nDate Posted: " + datePosted.toString() + "\n" + message + "\n";
    }

    public void storeMessage() {
        datePosted = new Date();
        String date = JDBCConnection.convertDate(datePosted);
        int max = 0;
        String query1 = "insert into Messages(data,tstamp,sender) values ('"+ message +"',"+ date +",'"+ ownerId+"')";
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query1);
            statement.close();
            max = JDBCConnection.maxMid();
        } catch (SQLException e) {
            System.out.println("Error wihle storing message. : " + e.toString());
        }
        messageId = max;
    }

    public void addTopics(ArrayList<Topic> topics) {
        // TODO: the message ID to these topics in database and create new topic entries if they don't exist.
        for(Topic topic : topics) {
            String query = "insert into Topic " +
                            " select '" + topic.getTopic() + "' from dual " +
                            " where not exists (select NULL " +
                            " from Topic where topic='" + topic.getTopic() + "')";
            String query2 = "insert into Topic_Message values (" +
                    "'" + topic.getTopic() + "'," + messageId + ")";

            try {
                Connection connection = JDBCConnection.createDBConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.executeUpdate(query2);
                statement.close();
            } catch(SQLException e) {
                System.out.println("Error in addtopics: " + e.toString());
            }
        }
    }
}
