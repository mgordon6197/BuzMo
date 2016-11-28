package com.oracle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public void postMessage(Message message, HashMap<String, User> postToUsersCircle, boolean isPublic) {

        System.out.println("In Post to Circle");

        // TODO: post message to my circle and other users circle.

    }
}
