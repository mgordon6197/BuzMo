package com.oracle;

/**
 *
 */
public class Request {
    private User userToAdd;
    private Addable addTo;
    private RequestType type;

    public User getUserToAdd() {
        return userToAdd;
    }

    public void setUserToAdd(User userToAdd) {
        this.userToAdd = userToAdd;
    }

    public Addable getAddTo() {
        return addTo;
    }

    public void setAddTo(Addable addTo) {
        this.addTo = addTo;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Request(User userToAdd, Addable addTo, RequestType type) {
        this.userToAdd = userToAdd;
        this.addTo = addTo;
        this.type = type;
    }

    public void acceptRequest() {
        // TODO: accept the request in the database
    }

    public void createRequest() {
        // TODO: create a request with the given fields.
    }
}
