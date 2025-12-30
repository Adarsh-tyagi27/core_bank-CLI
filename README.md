# CLI Bank Account Manager

A Java Console Application to demonstrate **Core Java**, **JDBC**, and **SQL** skills.

## Features
- **User Authentication**: Register and Login securely.
- **Account Management**: Real-time balance updates.
- **Transaction History**: Tracks Deposits and Withdrawals in a SQL database.
- **Data Persistence**: Uses SQLite (in `bank.db`), so data survives after the app closes.

## Coding Concepts Showcased
- **OOP Principles**: Encapsulation (Models), Separation of Concerns (DAOs).
- **JDBC**: Raw SQL interaction (PreparedStatement) to prevent SQL Injection.
- **Singleton Pattern**: (Implicitly in DatabaseConnection) for resource management.

## How to Run
### Option 1: Using IntelliJ IDEA / Eclipse (Recommended)
1. Open the project folder (`CLIBankManager`) in your IDE.
2. The IDE should detect `pom.xml` and automatically download the SQLite driver.
3. Locate `src/main/java/com/bank/Main.java`.
4. Right-click -> **Run 'Main.main()'**.

### Option 2: Command Line (Requires Maven)
1. Open terminal in this folder.
2. Run: `mvn clean install`
3. Run: `mvn exec:java -Dexec.mainClass="com.bank.Main"`

## Database
The project uses **SQLite**. 
- A file named `bank.db` will be created automatically in the project root options the first run.
- You can open this file with **DB Browser for SQLite** to see the raw data during your interview demo.
