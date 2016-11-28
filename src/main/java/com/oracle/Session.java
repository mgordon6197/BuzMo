package com.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
        Scanner scanner = new Scanner(java.lang.System.in);

        boolean logout = false;
        while(!logout) {
            java.lang.System.out.println(Constants.UserActionMenu);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("g"))
                selectExistingChatgroup();
            else if (input.equalsIgnoreCase("l"))
                logout = true;
            else
                selectManagerAction();
        }
    }

    private void selectExistingChatgroup() {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, ChatGroup> currentChatgroups = queryCurrentChatgroups();

        for(Map.Entry<String, ChatGroup> chatgroup : currentChatgroups.entrySet())
            System.out.println(chatgroup.getKey() + " : " + chatgroup.getValue().getName());

        System.out.print("Enter ChatGroup ID: ");
        String chatgroupId = scanner.nextLine();

        ChatGroup selectedChatgroup = currentChatgroups.get(chatgroupId);

        if(selectedChatgroup == null) {
            System.out.println("ChatGroup ID doesn't exist");
            return;
        }

        Date queryDateParam = new Date();
        ArrayList<Message> messages = selectedChatgroup.queryMessages(queryDateParam, true);

        for (Message message : messages)
            System.out.println(message.toString());

        boolean done = false;
        while(!done) {
            System.out.println(Constants.userChatGroupMessageOptions);
            if(currentUser.getUserId().equals(selectedChatgroup.getOwnerId()))
                System.out.println(Constants.ownerChatGroupOptions);

            String option = scanner.nextLine();
            if(option.equals("1")) {
                Message newMessage = createNewMessage();
                selectedChatgroup.postMessage(newMessage);
            }
            else if(option.equals("2")) {
                HashMap<String, User> friendsList = currentUser.queryFriends();
                for (Map.Entry<String, User> friend : friendsList.entrySet())
                    System.out.println(friend.getKey() + " : " + friend.getValue().getName());
                System.out.print("Enter user ID: ");
                String userID = scanner.nextLine();
                User friendToAdd = friendsList.get(userID);
                addFriendToChatGroup(friendToAdd, selectedChatgroup);
            }
            else if(option.equals("3")) {
                queryDateParam = messages.get(0).getDatePosted();
                messages = selectedChatgroup.queryMessages(queryDateParam, true);

                for (Message message : messages)
                    System.out.println(message.toString());
            }
            else if(option.equals("4")) {
                queryDateParam = messages.get(messages.size() - 1).getDatePosted();
                messages = selectedChatgroup.queryMessages(queryDateParam, false);

                for (Message message : messages)
                    System.out.println(message.toString());
            }
            else if(option.equals("5"))
                done = true;
            else
                System.out.println("INVALID OPTION\n");

            // TODO: implement chatgroup owner options.

        }

    }

    private void addFriendToChatGroup(User friendToAdd, ChatGroup chatGroup) {
        // TODO: invite friend to chatgroup.
    }

    private Message createNewMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Messsage:");
        String message = scanner.nextLine();
        return new Message(currentUser.getUserId(), message);
    }


    private HashMap<String, ChatGroup> queryCurrentChatgroups() {
        HashMap<String, ChatGroup> chatGroups = new HashMap<String, ChatGroup>();

        // test data
        chatGroups.put("chatgroupid", new ChatGroup("chatgroupownerid", "chatgroupid", "chatgroup name"));

        // TODO: select ChatGroups the current user is in. return hashmap of <chatgroupID, ChatGroup object>
        return chatGroups;
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

            addUser(username, password);
            System.out.println("Successfully added user " + username);

        } catch (SQLException e) {
            System.out.println("Could not create user " + username + "\n" + e.getMessage());
        }
    }

    private void addUser(String username, String password) throws SQLException {
        // TODO: add user into database
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
