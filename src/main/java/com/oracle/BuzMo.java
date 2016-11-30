package com.oracle;

import java.lang.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 *
 */
public class BuzMo
{
    public static void main(String[] argv) {
        Connection connection;
        try {
            connection = JDBCConnection.createDBConnection();
        } catch (SQLException e) {
            System.out.println(e.toString());
            return;
        }

        User loggedUser = null;
        boolean login = false;
        while (!login) {
            try {
                loggedUser = userLogin(connection);
                if(loggedUser == null)
                    continue;
                login = true;
            } catch (SQLException e) {
                System.out.println(e.toString());
                java.lang.System.out.println("Wrong Username/Password");
                continue;
            }
        }

        if (loggedUser != null) {
            Session session = new Session(loggedUser);
            session.start();
        }
    }

    private static User userLogin(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(java.lang.System.in);

        System.out.print("Log in as Manager(M) or User(U): ");
        String userType = scanner.nextLine();

        java.lang.System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        java.lang.System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // TODO: check if user is in database. also add necessary data to User or Manager objects.
        boolean isUser = false;
        boolean isManager = false;
        try {
            String query = "select email from Users where email='" + username + "'";
            Connection con = JDBCConnection.createDBConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next()) {
                isUser = true;
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("sqlexception during user login validation");
        }

        try {
            String query = "select userid from Manager where userid='" + username + "'";
            Connection con = JDBCConnection.createDBConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next()) {
                isManager = true;
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("sqlexception during user login validation");
        }

        if (userType.equalsIgnoreCase("u") && isUser)
            return new User(username);
        else if (userType.equalsIgnoreCase("m") && isUser && isManager)
            return new Manager(username);
        else {
            System.out.println("Incorrect user type\n");
            return null;
        }
    }
}
