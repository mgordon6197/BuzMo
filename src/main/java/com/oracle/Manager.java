package com.oracle;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class Manager extends User {
    public Manager(String userId) {
        super(userId);
    }

    public void addUser(String username, String password) {

        // TODO: add user to database
    }
}
