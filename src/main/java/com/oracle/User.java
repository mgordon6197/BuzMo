package com.oracle;

import java.util.*;

/**
 *
 */
public class User implements MessageQueryable{
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

        //test data
        friends.put("friendid", new User("friendid"));

        // TODO: query all the current users friends and create a HashMap with <UserID, UserObject>

        return friends;
    }

    public HashMap<String, ChatGroup> queryChatGroups() {
        HashMap<String, ChatGroup> chatGroups = new HashMap<String, ChatGroup>();

        // test data
        chatGroups.put("chatgroupid", new ChatGroup("chatgroupownerid", "chatgroupid", "chatgroup name"));

        // TODO: select ChatGroups the current user is in. return hashmap of <chatgroupID, ChatGroup object>

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
