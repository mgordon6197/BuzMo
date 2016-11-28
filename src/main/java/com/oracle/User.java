package com.oracle;

import java.util.HashMap;

/**
 *
 */
public class User {
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
}
