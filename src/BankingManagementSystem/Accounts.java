package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private final Connection connection;
    private final Scanner scanner;


    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccount(String email) {

        if (!accountExists(email)) {
            scanner.nextLine();
            System.out.println("Enter Full Name: ");
            String fullName = scanner.next();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();

            String query = "INSERT INTO accounts VALUES(?, ?, ?, ?,?)";

            try {
                long accountNumber = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Account opened successfully!!!");
                    return accountNumber;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }

            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        }
        throw new RuntimeException("Account Creation failed!!!");
    }

    public long getAccount_number(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    public long generateAccountNumber() {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM accounts " +
                    "ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                return resultSet.getLong("account_number") + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        return 10000100;
    }

    public boolean accountExists(String email) {
        try {
            String query = "SELECT * FROM accounts WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}
