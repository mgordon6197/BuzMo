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
public class BrowseUserSession implements MessageQueryable{
    private ArrayList<Topic> topics = null;
    private String queryUserId = null;

    public BrowseUserSession(ArrayList<Topic> topics) {
        this.topics = topics;
    }

    public BrowseUserSession(String queryUserId) {
        this.queryUserId = queryUserId;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.topics = topics;
    }

    public String getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }

    public ArrayList<User> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<User> u = new ArrayList<User>();

        if(queryUserId != null) {
            try {
                Connection connection = JDBCConnection.createDBConnection();
                Statement statement = connection.createStatement();
                String q = "select email from Users where email='"+queryUserId+"'";
                ResultSet result = statement.executeQuery(q);
                if(result.next()) {
                    u.add(new User(queryUserId));
                }
                statement.close();
                return u;
            } catch (SQLException e) {
                System.out.println("Bad exception in queryUserId querymessages... " + e.toString());
            }
        }

        String date = JDBCConnection.convertDate(queryDateParam);
        if(messagesOlderThan) {
            date = "M.tstamp <= " + date + " order by M.mid desc ";
        } else {
            date = "M.tstamp >= " + date + " order by M.mid asc ";
        }

        String query =
                " select M.sender " +
                " from Circle C, Topic_Message T, Message M " +
                " where C.messageid=T.messageid and C.messageid=M.mid and " + date;
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            ArrayList<String> users = new ArrayList<String>();
            while(result.next()) {
                users.add(result.getString("sender").trim());
            }

            for(String userid : users) {
                query = " select distinct T.topic" +
                        " from Topic_Message T, Message M" +
                        " where T.messageid=M.mid and M.sender='"+userid+"'";
                result = statement.executeQuery(query);
                ArrayList<Topic> userTopics = new ArrayList<Topic>();
                while(result.next()) {
                    userTopics.add(new Topic(result.getString("topic").trim()));
                }

                boolean addUser = false;
                for(Topic str : topics) {
                    if(userTopics.contains(str.getTopic())) {
                        addUser = true;
                        break;
                    }
                }
                if(addUser) {
                    u.add(new User(userid));
                }
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("bad exceeeption:" + e.toString());
        }

        return u;
    }

    public void postMessage(Message message) {
        // DONT IMPPLEMENT MEEEE
    }
}
