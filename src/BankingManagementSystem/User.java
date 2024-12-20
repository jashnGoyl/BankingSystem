package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private final Connection connection;
    private final Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("Full Name: ");
        String full_name = scanner.next();
        scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.next();
        scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.next();
        scanner.nextLine();

        if(userExists(email)){
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }

        try{
            String registerQuery = "INSERT INTO users VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
            preparedStatement.setString(1,email);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("User registration successful!!!");
            }else{
                System.out.println("User registration failed!!!");
            }
        }catch(SQLException e){
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch (SQLException e){
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    public boolean userExists(String email){
        try{
            String registerQuery = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }catch(SQLException e){
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}
