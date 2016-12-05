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
        String query = "insert into Users values " +
                "('"+username+"','"+username+"','"+password+"',"+ "'400'" + ",'boo')";
        try {
            Connection con = JDBCConnection.createDBConnection();
            Statement statement = con.createStatement();
            statement.executeQuery(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("BAD USER");
        }
    }
}
