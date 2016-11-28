package com.oracle;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matthew on 11/22/2016.
 */
public class Manager extends User {
    public Manager(String userId) {
        super(userId);
    }

    public void addUser(String username, String password) {

        // TODO: add user to database
    }
}
