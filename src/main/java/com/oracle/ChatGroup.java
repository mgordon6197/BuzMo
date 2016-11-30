package com.oracle;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class ChatGroup implements MessageQueryable{
    String ownerId;
    String name;
    int duration;

    public ChatGroup(String ownerId, String name, int duration) {
        this.ownerId = ownerId;
        this.name = name;
        this.duration = duration;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan) {

        ArrayList<Message> messages = new ArrayList<Message>();

        // test data
        messages.add(new Message("messageownerid", "Hello this is a message."));

        // TODO: query most recent messages in this chat in order of oldest first.

        return messages;
    }

    public void postMessage(Message message) {

        // TODO: store the message in the database.
    }

    public void updateDuration(String newDuration) {

        // TODO: update chatgroup row with newDuration.
    }

    public void updateName(String newName) {

        // TODO: update chatgroup row with newName.
    }

    public void addFriendToChatGroup(User friendToAdd) {

        // TODO: invite friend to chatgroup.
    }

    public void createChatGroup(ChatGroup newChatgroup) {
        // TODO: create chatgroup from passed ChatGroup object.
    }
}
