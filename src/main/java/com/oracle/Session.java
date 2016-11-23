package com.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 */
public class Session {
    private User currentUser;

    public Session(User loggedUser) {
        currentUser = loggedUser;
    }

    public void start() {
        if(currentUser instanceof Manager)
            selectManagerAction();
        else
            selectUserAction();
    }

    private void selectManagerAction(){
        Scanner scanner = new Scanner(java.lang.System.in);

        boolean logout = false;
        while(!logout) {
            java.lang.System.out.println(Constants.ManagerActionMenu);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("c"))
                createUserAction();
            else if (input.equalsIgnoreCase("l"))
                logout = true;
            else
                selectManagerAction();
        }
    }

    private void selectUserAction() {

    }

    private void createUserAction(){
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        try {
            System.out.print("Enter new Username: ");
            username = scanner.nextLine();

            System.out.print("Enter Password: ");
            password = scanner.nextLine();

            System.out.print("Enter Password again: ");
            String secondPass = scanner.nextLine();

            if(!validateUser(username, password, secondPass))
                return;

            Connection connection = JDBCConnection.createDBConnection();
            // TODO: add user into database
            System.out.println("Successfully added user " + username);

        } catch (SQLException e) {
            System.out.println("Could not create user " + username + "\n" + e.getMessage());
        }
    }

    private boolean validateUser(String username, String password, String secondPass) {
        if(!password.equals(secondPass)) {
            System.out.println("\nPasswords do not match\n");
            return false;
        }
        else if(password.length() <= 0) {
            System.out.println("\nPassword invalid\n");
            return false;
        }

        return true;
    }
}
