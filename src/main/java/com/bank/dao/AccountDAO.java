package com.bank.dao;

import com.bank.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public void createAccount(int userId) {
        String sql = "INSERT INTO accounts(user_id, balance) VALUES(?, 0.0)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("Account created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
    }

    public double getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching balance: " + e.getMessage());
        }
        return 0.0;
    }

    public void deposit(int userId, double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            recordTransaction(userId, "DEPOSIT", amount);
            System.out.println("Deposited $" + amount);
        } catch (SQLException e) {
            System.err.println("Deposit failed: " + e.getMessage());
        }
    }

    public void withdraw(int userId, double amount) {
        double currentBalance = getBalance(userId);
        if (currentBalance < amount) {
            System.out.println("Insufficient funds!");
            return;
        }

        String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            recordTransaction(userId, "WITHDRAW", amount);
            System.out.println("Withdrawn $" + amount);
        } catch (SQLException e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
        }
    }

    // A private helper to find account ID from User ID would be better, but for
    // simplicity:
    // We assume 1 account per user for now or just grab the first one.
    private void recordTransaction(int userId, String type, double amount) {
        // First get account number
        int accountId = -1;
        String getAcc = "SELECT account_number FROM accounts WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement p1 = conn.prepareStatement(getAcc)) {
            p1.setInt(1, userId);
            ResultSet rs = p1.executeQuery();
            if (rs.next())
                accountId = rs.getInt("account_number");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (accountId != -1) {
            String sql = "INSERT INTO transactions(account_id, type, amount) VALUES(?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, accountId);
                pstmt.setString(2, type);
                pstmt.setDouble(3, amount);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Transaction log failed: " + e.getMessage());
            }
        }
    }
}
