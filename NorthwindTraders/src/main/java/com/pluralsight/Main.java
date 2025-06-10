package com.pluralsight;


import java.sql.*;
import java.util.Scanner;

public class Main {
    private static sqlConnectionInfo sqlConnectionInfo;
    public static void main(String[] args) {
      
        if(args.length != 3){
            System.out.println(
                    "Application needs three arguments to run" +
                            "java.com.pluralsight.Main <username> <password> <sqlURL>"
            );
            System.exit(1);
        }

        sqlConnectionInfo = getSqlConnectionInfo(args);

        try {
            userDefinedQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static sqlConnectionInfo getSqlConnectionInfo(String[] args){
        String username = args[0];
        String password = args[1];
        String connectionString = args[2];
        
        return new sqlConnectionInfo(connectionString, username, password);
    }
    
    private static void userDefinedQuery() throws SQLException {
        Scanner s = new Scanner(System.in);
        // prompt user to choose query
        System.out.println("""
                    What data would you like to choose?
                    [1] Display all products
                    [2] Display all customers
                    [0] Exit
                    """);

        int queryChoice = s.nextInt();

        switch (queryChoice){
            case 1 -> displayProducts();
            case 2 -> displayCustomers();
            case 0 -> {
                System.out.println("Exiting Application..."); break;}
            default -> System.out.println("Invalid entry. Try again.");
        }
    }
        
    private static void displayProducts() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        
        try {
            // load for MySQL drive
            Class.forName("com.mysql.cj.jdbc.Driver");

            // open a connection to the database
            // use database URL to point to correct database
            connection = DriverManager.getConnection(
                    sqlConnectionInfo.getConnectionString(),
                    sqlConnectionInfo.getUsername(),
                    sqlConnectionInfo.getPassword());
            
            String query = "SELECT ProductID, ProductName, QuantityPerUnit, UnitPrice FROM products";

            statement = connection.prepareStatement(query);
            
            
            // execute query
            if (statement != null) {
                results = statement.executeQuery(query);
            }

            if (results != null) {
                while(results.next()){
                    int ProductID = results.getInt("ProductID");
                    String ProductName = results.getString("ProductName");
                    String QuantityPerUnit = results.getString("QuantityPerUnit");
                    int UnitPrice = results.getInt("UnitPrice");

                    System.out.printf("""
                ProductID: %s
                ProductName: %s
                Quantity Per Unit: %s
                Unit Price: %d
                ----------------
                """,ProductID, ProductName, QuantityPerUnit, UnitPrice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        finally {
            if (results != null) {
                results.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    private static void displayCustomers() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            // load for MySQL drive
            Class.forName("com.mysql.cj.jdbc.Driver");

            // open a connection to the database
            // use database URL to point to correct database
            connection = DriverManager.getConnection(
                    sqlConnectionInfo.getConnectionString(),
                    sqlConnectionInfo.getUsername(),
                    sqlConnectionInfo.getPassword());

            String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers";

            statement = connection.prepareStatement(query);


            // execute query
            if (statement != null) {
                results = statement.executeQuery(query);
            }

            // process the results
            if (results != null) {
                while (results.next()) {
                    String contactName = results.getString("ContactName");
                    String companyName = results.getString("CompanyName");
                    String city = results.getString("City");
                    String country = results.getString("Country");
                    String phoneNumber = results.getString("Phone");

                    System.out.printf("""
                            Contact Name: %s
                            Company Name: %s
                            City: %s
                            Country %s
                            Phone Number: %s
                            ---------------------
                            """, contactName, companyName, city, country, phoneNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (results != null) {
                results.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}