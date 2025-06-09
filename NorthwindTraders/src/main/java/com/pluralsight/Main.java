package com.pluralsight;


import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Connection to the Northwind database
        // information to connect to the database
        String connectionString = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "yearup";

        // load for MySQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // open a connection to the database
        // use database URL to point to correct database
        Connection connection;
        connection = DriverManager.getConnection(connectionString, username, password);

        // create statement
        // the statement is tied to the open connection
        Statement statement = connection.createStatement();

        // define your query
        String query = "SELECT ProductName FROM products ";

        // execute query
        ResultSet results = statement.executeQuery(query);

        // process the results
        while (results.next()){
            String product = results.getString("ProductName");
            System.out.println(product);
        }
        
        // close the connection
        connection.close();
    }
}