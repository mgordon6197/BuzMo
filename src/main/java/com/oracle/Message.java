package com.oracle;

import java.util.Date;

/**
 *
 */
public class Message {
    private String ownerId;
    private String message;
    private Date datePosted;

    public Message(String ownerId, String message) {
        this.ownerId = ownerId;
        this.message = message;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
