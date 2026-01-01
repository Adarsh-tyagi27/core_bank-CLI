-- Database Schema Design

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- Auto-incrementing ID
    username TEXT NOT NULL UNIQUE,        -- Unique username
    password TEXT NOT NULL,               -- Hashed password (in real app)
    full_name TEXT NOT NULL
);

-- Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    account_number INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    balance REAL DEFAULT 0.0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER,
    type TEXT NOT NULL CHECK(type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
    amount REAL NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_number)
);
