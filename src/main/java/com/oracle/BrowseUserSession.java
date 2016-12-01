package com.oracle;

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

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> messages = new ArrayList<Message>();

        // TODO: query Users based off either the email or topics in the instance variables. Only one of them will be initialized.

        return messages;
    }

    public void postMessage(Message message) {

    }

    public ArrayList<User> queryUsers() {

        ArrayList<User> Users = new ArrayList<User>();

        // TODO: query Users based off either the email or topics in the instance variables. Only one of them will be initialized.

        return Users;
    }
}
