package com.pluralsight;


import java.sql.*;
import java.util.Scanner;

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

        String categoryID;
        
        Scanner s = new Scanner(System.in);

        System.out.println("What category ID?");
        
        categoryID = s.nextLine();
        
        // define your query
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products WHERE categoryID = " + categoryID;

        // execute query
        ResultSet results = statement.executeQuery(query);

        // process the results
        while (results.next()){
            String productID = results.getString("ProductID");
            String productName = results.getString("ProductName");
            String UnitPrice = results.getString("UnitPrice");
            String UnitsInStock = results.getString("UnitsInStock");


            System.out.printf("""
                    Product ID: %s
                    Name: %s
                    Price: $%.5s
                    Stock: %s
                    -------------------
                    """, productID, productName, UnitPrice, UnitsInStock);
        }
        
        // close the connection
        connection.close();
    }
}