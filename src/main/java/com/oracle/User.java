package com.oracle;

import java.util.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 */
public class User implements MessageQueryable{
    private String userId;
    private String name;
    public static Connection con;

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

        return friends;
    }

    public HashMap<String, ChatGroup> queryChatGroups() {
        HashMap<String, ChatGroup> chatGroups = new HashMap<String, ChatGroup>();
        String query = "select M.gname,G.userid from Member_Of M,Group_Owner G " +
                       "where M.userid = '" + userId + "' and G.gname = M.gname";
        System.out.println(query);
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String owner = result.getString("userid");
                String group = result.getString("gname");
                chatGroups.put(group,new ChatGroup(owner,group,group));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chatGroups;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> circleMessages = new ArrayList<Message>();

        // Test Data
        circleMessages.add(new Message(userId, "Circle Message"));

        // TODO: query this users circle. Sort by oldest first.

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

            System.out.println("select friends to post in their circle. Delimit by a comma.");
            String usersString = scanner.nextLine();
            String [] userIds = usersString.replaceAll("\\s+","").split(",");

            for( String userId : userIds)
                postToUsersCicle.put(userId, new User(userId));
        }

        postMessage(message, postToUsersCicle, isPublic);
    }


    public void postMessage(Message message, HashMap<String, User> postToUsersCircle, boolean isPublic) {



        System.out.println("In Post to Circle");

        // TODO: post message to my circle and other users circle.

    }
}
