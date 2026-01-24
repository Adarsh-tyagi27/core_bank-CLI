package com.bank;

import com.bank.dao.AccountDAO;
import com.bank.dao.UserDAO;
import com.bank.model.User;
import com.bank.util.DatabaseConnection;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static AccountDAO accountDAO = new AccountDAO();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("Initializing Banking System...");
        DatabaseConnection.initializeDatabase();

        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showAuthMenu() {
        System.out.println("\n=== WELCOME TO JAVA BANK ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void register() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String fullname = scanner.nextLine();

        User newUser = new User(username, password, fullname);
        if (userDAO.registerUser(newUser)) {
            System.out.println("Registration Successful! Please login.");
            // Auto-create an empty account for the new user
            User tempUser = userDAO.loginUser(username, password);
            if (tempUser != null) {
                accountDAO.createAccount(tempUser.getId());
            }
        }
    }

    private static void login() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userDAO.loginUser(username, password);
        if (user != null) {
            System.out.println("Login Successful! Welcome, " + user.getFullName());
            currentUser = user;
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU (" + currentUser.getFullName() + ") ===");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1:
                double balance = accountDAO.getBalance(currentUser.getId());
                System.out.println("Current Balance: $" + balance);
                break;
            case 2:
                System.out.print("Enter amount to deposit: ");
                try {
                    double depositAmount = Double.parseDouble(scanner.nextLine());
                    accountDAO.deposit(currentUser.getId(), depositAmount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                }
                break;
            case 3:
                System.out.print("Enter amount to withdraw: ");
                try {
                    double withdrawAmount = Double.parseDouble(scanner.nextLine());
                    accountDAO.withdraw(currentUser.getId(), withdrawAmount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                }
                break;
            case 4:
                currentUser = null;
                System.out.println("Logged out.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
