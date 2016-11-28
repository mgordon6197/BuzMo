package com.oracle;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class PrivateConversation implements MessageQueryable {
    String currentUserId;
    String otherUserId;

    public PrivateConversation(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {
        ArrayList<Message> privateMessages = new ArrayList<Message>();

        // Test Data
        privateMessages.add(new Message(currentUserId, "Private Message"));

        // TODO: query private messages for the given two users.

        return privateMessages;
    }

    public void postMessage(Message message) {
        System.out.println("In Private Conversation Post Message");

        // TODO: post message for the given two users private conversation.
    }
}
