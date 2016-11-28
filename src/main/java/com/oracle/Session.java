package com.oracle;

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
            System.out.println(Constants.ManagerActionMenu);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("c"))
                createUserAction();
            else if (input.equalsIgnoreCase("l"))
                logout = true;
            else
                System.out.println("Invalid action");
        }
    }

    private void selectUserAction() {
        Scanner scanner = new Scanner(java.lang.System.in);

        boolean logout = false;
        while(!logout) {
            System.out.println(Constants.UserActionMenu);
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("g"))
                selectExistingChatgroup();
            if(input.equalsIgnoreCase("c"))
                viewCircleAction();
            else if(input.equalsIgnoreCase("l"))
                logout = true;
            else
                System.out.println("Invalid action");
        }
    }

    private void viewCircleAction() {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, User> friendsList = currentUser.queryFriends();
        System.out.println(currentUser.getUserId() + " : My Circle" );
        for (Map.Entry<String, User> friend : friendsList.entrySet())
            System.out.println(friend.getKey() + " : " + friend.getValue().getName());

        System.out.println("\nSelect User ID: ");
        String userId = scanner.nextLine();

        ArrayList<Message> circleMessages;
        if(userId.equals(currentUser.getUserId()))
            circleMessages = currentUser.queryMessages(new Date(), false);
        else {
            User circleUser = new User(userId);
            circleMessages = circleUser.queryMessages(new Date(), false);
        }

        if(circleMessages == null) {
            System.out.println("Invalid User ID");
            return;
        }

        for(Message circleMessage : circleMessages)
            System.out.println(circleMessage.toString());

        inViewCircleOptions(circleMessages);

    }

    private void inViewCircleOptions(ArrayList<Message> circleMessages) {
        Scanner scanner = new Scanner(System.in);

        boolean done = false;
        while(!done) {
            System.out.println(Constants.insideViewCircleMenu);
            String option = scanner.nextLine();
            if (option.equals("1"))
                postMessageToCircle();
            else if (option.equals("2")) {
                ArrayList<Message> messages = scrollUp(circleMessages, currentUser);
                for(Message message : messages)
                    System.out.println(message.toString());
            }
            else if (option.equals("3")) {
                ArrayList<Message> messages = scrollDown(circleMessages, currentUser);
                for(Message message : messages)
                    System.out.println(message.toString());
            }
            else if (option.equals("4"))
                done = true;
            else
                System.out.println("Invalid Option");
        }

    }

    private void postMessageToCircle() {
        Message message = createNewMessage();

        Scanner scanner = new Scanner(System.in);

        HashMap<String, User> postToUsersCicle = new HashMap<String, User>();
        System.out.println("Make message public?(y/n)");
        String input = scanner.nextLine();
        boolean isPublic = false;
        if(input.equalsIgnoreCase("y")) {
            isPublic = true;
            postToUsersCicle = currentUser.queryFriends();
        }
        else {
            HashMap<String, User> allFriends = currentUser.queryFriends();
            for(Map.Entry<String, User> user : allFriends.entrySet())
                System.out.println(user.getKey() + " : " + user.getValue().getName());

            System.out.println("select friends to post in their circle. Delimit by a comma.");
            String usersString = scanner.nextLine();
            String [] userIds = usersString.replaceAll("\\s+","").split(",");

            for( String userId : userIds)
                postToUsersCicle.put(userId, new User(userId));
        }

        currentUser.postMessage(message, postToUsersCicle, isPublic);
    }

    private void selectExistingChatgroup() {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, ChatGroup> currentChatgroups = currentUser.queryChatGroups();

        for(Map.Entry<String, ChatGroup> chatgroup : currentChatgroups.entrySet())
            System.out.println(chatgroup.getKey() + " : " + chatgroup.getValue().getName());

        System.out.print("Enter ChatGroup ID: ");
        String chatgroupId = scanner.nextLine();

        ChatGroup selectedChatgroup = currentChatgroups.get(chatgroupId);

        if(selectedChatgroup == null) {
            System.out.println("ChatGroup ID doesn't exist");
            return;
        }

        inChatGroupOptions(selectedChatgroup);

    }

    private void inChatGroupOptions(ChatGroup selectedChatgroup) {

        // prints out most recent messages in chatgroup
        Date queryDateParam = new Date();
        ArrayList<Message> messages = selectedChatgroup.queryMessages(queryDateParam, true);
        for (Message message : messages)
            System.out.println(message.toString());


        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done) {
            System.out.println(Constants.userChatGroupMessageOptions);
            if(currentUser.getUserId().equals(selectedChatgroup.getOwnerId()))
                System.out.println(Constants.ownerChatGroupOptions);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String option = scanner.nextLine();
            // post a new message to the chatgroup
            if(option.equals("1")) {
                Message newMessage = createNewMessage();
                selectedChatgroup.postMessage(newMessage);
            }
            // add a friend to the chat group
            else if(option.equals("2")) {
                HashMap<String, User> friendsList = currentUser.queryFriends();
                for (Map.Entry<String, User> friend : friendsList.entrySet())
                    System.out.println(friend.getKey() + " : " + friend.getValue().getName());

                System.out.print("Enter user ID: ");
                String userID = scanner.nextLine();
                User friendToAdd = friendsList.get(userID);

                selectedChatgroup.addFriendToChatGroup(friendToAdd);
            }
            // scroll up (get next set of older messages)
            else if(option.equals("3")) {
                messages = scrollUp(messages, selectedChatgroup);
                for (Message message : messages)
                    System.out.println(message.toString());
            }
            // scroll down (get next set of newer messages)
            else if(option.equals("4")) {
                messages = scrollDown(messages, selectedChatgroup);
                for (Message message : messages)
                    System.out.println(message.toString());
            }
            // back to main menu
            else if(option.equals("5"))
                done = true;
            // update chatgroup message duration
            else if(option.equals("6") && currentUser.getUserId().equals(selectedChatgroup.getOwnerId())) {
                System.out.print("Enter new ChatGroup message duration(seconds): ");
                String newDuration = scanner.nextLine();
                selectedChatgroup.updateDuration(newDuration);
            }
            // update chatgroup name
            else if(option.equals("7") && currentUser.getUserId().equals(selectedChatgroup.getOwnerId())) {
                System.out.print("Enter new ChatGroup name: ");
                String newName = scanner.nextLine();
                selectedChatgroup.updateName(newName);
            }
            else
                System.out.println("INVALID OPTION\n");


        }
    }

    private ArrayList<Message> scrollDown(ArrayList<Message> messages, MessageQueryable messageQueryable) {
        Date queryDateParam = messages.get(messages.size() - 1).getDatePosted();
        return messageQueryable.queryMessages(queryDateParam, false);
    }

    private ArrayList<Message> scrollUp(AbstractList<Message> messages, MessageQueryable messageQueryable) {
       Date queryDateParam = messages.get(0).getDatePosted();
        return messageQueryable.queryMessages(queryDateParam, true);
    }

    private Message createNewMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Messsage:");
        String message = scanner.nextLine();
        return new Message(currentUser.getUserId(), message);
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

        if(currentUser instanceof Manager) {
            Manager manager = (Manager)currentUser;
            manager.addUser(username, password);
        }
        else
            System.out.println("User has invalid privileges to create another user.");
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