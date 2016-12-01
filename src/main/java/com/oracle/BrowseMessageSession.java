package com.oracle;

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

        return messages;
    }

    public void postMessage(Message message) {

    }
}
