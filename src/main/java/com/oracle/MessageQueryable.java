package com.oracle;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public interface MessageQueryable {

    ArrayList<Message> queryMessages(Date queryDateParam, boolean messagesOlderThan);

    void postMessage(Message newMessage);
}
