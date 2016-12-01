package com.oracle;

import java.util.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 */
public class User implements MessageQueryable, Addable{
    private String userId;
    private String name;

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, User> queryFriends() {
        HashMap<String, User> friends = new HashMap<String, User>();

        // TODO: query all the current users friends and create a HashMap with <UserID, UserObject>

        String query = "select user2id from Is_friends where user1id='"+userId+"'";
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                String id = result.getString("user2id").trim();
                friends.put(id,new User(id));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Bad exception, query friends: " + e.toString());
        }

        return friends;
    }

    public HashMap<String, ChatGroup> queryChatGroups() {
        HashMap<String, ChatGroup> chatGroups = new HashMap<String, ChatGroup>();
        String query = "select M.gname,G.userid,G.duration from Member_Of M,Group_Owner G " +
                       "where M.userid = '" + userId + "' and G.gname = M.gname";
        Statement statement = null;
        try {
            statement = JDBCConnection.createDBConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String owner = result.getString("userid").trim();
                String group = result.getString("gname").trim();
                int duration = result.getInt("duration");
                chatGroups.put(group,new ChatGroup(owner,group,duration));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chatGroups;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> circleMessages = new ArrayList<Message>();
        // get circle messages where userid,messageid in circle,
        // but make sure the messages match from over in messages, and the date is correct
        String date = JDBCConnection.convertDate(queryDateParam);
        if(messagesOlderThan) {
            date = "M.tstamp <= " + date + " order by M.mid desc ";
        } else {
            date = "M.tstamp >= " + date + " order by M.mid asc ";
        }
        String query =
                "select M.mid,M.sender,M.data, M.tstamp " +
                        "from Circle C, Messages M " +
                        "where C.userid = '" + userId + "' and " +
                        "C.messageid = M.mid and " +
                        date +
                        " ";
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                String mOwner = result.getString("sender").trim();
                String mData = result.getString("data").trim();
                Date mDate = result.getTimestamp("tstamp");
                // TODO GET TOPICS WORDS TOO
                Message message = new Message(mOwner, mData);
                message.setDatePosted(mDate);
                circleMessages.add(message);
            }

        } catch (SQLException e) {
            System.out.println("Error in circle: " + e.toString());
        }

        return circleMessages;
    }

    public void postMessage(Message message) {

        Scanner scanner = new Scanner(System.in);

        HashMap<String, User> postToUsersCicle = new HashMap<String, User>();
        System.out.println("Make message public?(y/n)");
        String input = scanner.nextLine();
        boolean isPublic = false;
        if(input.equalsIgnoreCase("y")) {
            isPublic = true;
            postToUsersCicle = queryFriends();
        }
        else {
            HashMap<String, User> allFriends = queryFriends();
            for(Map.Entry<String, User> user : allFriends.entrySet())
                System.out.println(user.getKey() + " : " + user.getValue().getName());

            System.out.println("Select friends to post in their circle. Delimit by a comma.");
            String usersString = scanner.nextLine();
            String [] userIds = usersString.replaceAll("\\s+","").split(",");

            for( String userId : userIds)
                postToUsersCicle.put(userId, new User(userId));
        }

        System.out.println("Select topics to define this message. Delimit by a comma.");
        String topicsString = scanner.nextLine();
        String [] topicAsStrings = topicsString.replaceAll("\\s+","").split(",");

        ArrayList<Topic> topics = new ArrayList<Topic>();
        for(String topic : topicAsStrings)
            topics.add(new Topic(topic));
        message.addTopics(topics);

        postMessage(message, postToUsersCicle, isPublic);
    }

    private void postMessage(Message message, HashMap<String, User> postToUsersCircle, boolean isPublic) {
        System.out.println("In Post to Circle");

        postToUsersCircle.put(userId,null);
        for(Map.Entry<String,User>user : postToUsersCircle.entrySet()) {
            String query = "insert into Circle values (" +
                    "'"+user.getKey()+"',"+message.getMessageId();
            if(isPublic)
                query += ",1)";
            else
                query += ",0)";
            try {
                Connection connection = JDBCConnection.createDBConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } catch (SQLException e) {
                System.out.println("error in postMessage to circle: " + e.toString());
            }
        }

    }

    public ArrayList<Request> viewRequests() {
        ArrayList<Request> requests = new ArrayList<Request>();

        // TODO: select all of this users requests and put them into Request objects and return them.

        try {
            String query1 = "select sender from Friend_Requests " +
                    " where receiver = '" + userId + "'";
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query1);
            while(result.next()) {
                String sender = result.getString("sender");
                requests.add(new Request(new User(userId),new User(sender),RequestType.FRIEND_REQUEST));
            }
            statement.close();

            String query2 = "select group_name from Chatgroup_Requests " +
                    " where receiver = '" + userId + "'";
            statement = connection.createStatement();
            result = statement.executeQuery(query2);
            while(result.next()) {
                String group_name = result.getString("group_name").trim();
                ChatGroup chat = new ChatGroup(group_name,group_name,7);

                requests.add(new Request(new User(userId),chat,RequestType.CHATGROUP_INVITE));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in viewRequests, User: " + e.toString());
        }

        return requests;
    }

    public String returnId() {
        return userId;
    }

    public void addFriend(String userId) {

        // TODO: add friend to friends list. assumes the users are not already friends but doesn't guarantee that the passed userId exists.
        Request friendRequest = new Request(new User(userId), this, RequestType.FRIEND_REQUEST);
        friendRequest.createRequest();
    }
}
