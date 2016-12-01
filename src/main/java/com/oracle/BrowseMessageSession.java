package com.oracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class BrowseMessageSession implements MessageQueryable {

    private ArrayList<Topic> topics;
    private boolean matchAll;

    public BrowseMessageSession(ArrayList<Topic> topics, boolean matchAll) {
        this.topics = topics;
        this.matchAll = matchAll;
    }

    public boolean isMatchAll() {
        return matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.topics = topics;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {

        ArrayList<Message> messages = new ArrayList<Message>();
        // TODO: query all messages based off the topics and whether it needs to match all of them or not.
        String date = JDBCConnection.convertDate(queryDateParam);
        if(messagesOlderThan) {
            date = "M.tstamp <= " + date + " order by M.mid desc ";
        } else {
            date = "M.tstamp >= " + date + " order by M.mid asc ";
        }


        // get public messages from circle
        boolean good = true;
        String query =
                " select distinct M.mid,M.data,M.sender,M.tstamp" +
                " from Messages M,Circle C" +
                " where M.mid=C.messageid and C.pflag=1 " +
                        " and " + date ;
        ArrayList<Message> messageList = new ArrayList<Message>();

        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                String data = result.getString("data").trim();
                String sender = result.getString("sender").trim();
                int mid = result.getInt("mid");
                Date d = result.getTimestamp("tstamp");
                Message m = new Message(sender,data);
                m.setDatePosted(d);
                m.setMessageId(mid);
                messageList.add(m);
            }

            for(Message m : messageList) {
                query = " select T.topic from Topic_Message T where T.messageid=" + m.getMessageId();
                result = statement.executeQuery(query);
                ArrayList<String> topicList = new ArrayList<String>();
                while(result.next()) {
                    topicList.add(result.getString("topic").trim());
                }

                boolean addMessages;
                if(matchAll) {
                    addMessages = true;
                    for(Topic str : topics) {
                        if(!topicList.contains(str.getTopic())) {
                            addMessages = false;
                            break;
                        }
                    }
                } else {
                    addMessages = false;
                    for(Topic str : topics) {
                        if(topicList.contains(str.getTopic())) {
                            addMessages = true;
                            break;
                        }
                    }
                }

                if(addMessages) {
                    messages.add(m);
                }
                if(messages.size() >= 7) {
                    break;
                }
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Bad exception, browsing massages: " + e.toString());
        }

        return messages;
    }

    public void postMessage(Message message) {

    }
}
