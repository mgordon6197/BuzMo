package com.oracle;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

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
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();

            if(type == RequestType.FRIEND_REQUEST) {
                // remove friend request from Friend_Requests
                String removeQuery = "delete from Friend_Requests " +
                        " where sender = '" + userToAdd.getUserId() + "' " +
                        " and receiver = '" + addTo.returnId() + "'";
                // then add me,friend and friend,me to Is_friends
                String insertQuery1 = "insert into Is_friends values " +
                        " ('" + userToAdd.getUserId() + "','" + addTo.returnId() +"')";
                String insertQuery2 = "insert into Is_friends values " +
                        " ('" + addTo.returnId() + "','" + userToAdd.getUserId() +"')";

                statement.execute(removeQuery);
                statement.executeUpdate(insertQuery1);
                statement.executeUpdate(insertQuery2);
                statement.close();

            } else if(type == RequestType.CHATGROUP_INVITE) {
                // remove chatgroup requests
                String removeQuery = "delete from Chatgroup_Requests " +
                        " where receiver = '" + userToAdd.getUserId() + "' " +
                        " and group_name = '" + addTo.returnId() + "'";
                // then add user to chatgroup
                String insertQuery = "insert into Member_of values " +
                        " ('" + addTo.returnId() + "','" + userToAdd.getUserId() + "')";
                statement.execute(removeQuery);
                statement.executeUpdate(insertQuery);
                statement.close();
            }

        } catch (SQLException e) {
            System.out.println("sql exception in createRequest: " + e.toString());
        }
    }

    public void createRequest() {
        try {
            Connection connection = JDBCConnection.createDBConnection();
            Statement statement = connection.createStatement();

            if(type == RequestType.FRIEND_REQUEST) {
                // assume we're not friends
                // insert into friend requests
                String query1 = "insert into Friend_Requests " +
                        " select '" + addTo.returnId() + "','" + userToAdd.getUserId() + "' from dual " +
                        " where not exists (select NULL " +
                        " from Friend_Requests where sender='" + addTo.returnId() + "' and " +
                                                        " receiver='" + userToAdd.getUserId() + "')" +
                        " and not exists (select NULL " +
                        " from Friend_Requests where sender='" + userToAdd.getUserId() + "' and " +
                                                        " receiver='" + addTo.returnId() + "')";
                System.out.println(query1);
                statement.executeQuery(query1);
                statement.close();
            } else if(type == RequestType.CHATGROUP_INVITE) {
                // assume we're friends with them
                // insert into chatgroup_requests unless already in there
                String query1 = "insert into Chatgroup_Requests " +
                        " select '" + userToAdd.getUserId() + "','" + addTo.returnId() + "' from dual " +
                        " where not exists (select NULL " +
                        " from Chatgroup_Requests where receiver='" + userToAdd.getUserId() + "' and " +
                                                        " group_name='" + addTo.returnId() + "')" +
                        " and not exists (select NULL " +
                        " from Member_Of where gname='" + addTo.returnId() + "' and " +
                                                        " userid='" + userToAdd.getUserId() + "')";
                System.out.println(query1);
                statement.executeQuery(query1);
                statement.close();
            }

        } catch (SQLException e) {
            System.out.println("sql exception in createRequest: " + e.toString());
        }
    }
}
