package com.oracle;

import java.lang.*;
import java.sql.Connection;
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
                loggedUser.con = connection;
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

        if (userType.equalsIgnoreCase("u"))
            return new User(username);
        else if (userType.equalsIgnoreCase("m"))
            return new Manager(username);
        else {
            System.out.println("Incorrect user type\n");
            return null;
        }
    }
}
