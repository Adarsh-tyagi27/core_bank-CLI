package com.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // This will create a file named 'bank.db' in the project root
    private static final String URL = "jdbc:sqlite:bank.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "full_name TEXT NOT NULL"
                + ");";

        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "balance REAL DEFAULT 0.0,"
                + "FOREIGN KEY (user_id) REFERENCES users(id)"
                + ");";
        
        // Simple transaction history table
        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "account_id INTEGER,"
                + "type TEXT NOT NULL," // DEPOSIT, WITHDRAW, TRANSFER
                + "amount REAL NOT NULL,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_number)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
