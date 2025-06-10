package com.pluralsight;


import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final Scanner s = new Scanner(System.in);
    private static BasicDataSource basicDataSource;

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println(
                    "Application needs three arguments to run" +
                            "java.com.pluralsight.Main <username> <password> <sqlURL>"
            );
            System.exit(1);
        }

        basicDataSource = getBasicDataSourceFromArgs(args);

        try {
            userDefinedQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static BasicDataSource getBasicDataSourceFromArgs(String[] args) {
        String username = args[0];
        String password = args[1];
        String connectionString = args[2];
        
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(connectionString);

        return dataSource;
    }

    private static void userDefinedQuery() throws SQLException {
        // prompt user to choose query
        System.out.println("""
                What data would you like to choose?
                [1] Display all Products
                [2] Display all Customers
                [3] Display all Categories
                [0] Exit
                """);

        int queryChoice = s.nextInt();

        switch (queryChoice) {
            case 1 -> displayProducts();
            case 2 -> displayCustomers();
            case 3 -> displayCategories();
            case 0 -> System.out.println("Exiting Application...");
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
            connection = basicDataSource.getConnection();

            String query = "SELECT ProductID, ProductName, QuantityPerUnit, UnitPrice FROM products";

            statement = connection.prepareStatement(query);


            // execute query
            if (statement != null) {
                results = statement.executeQuery(query);
            }

            if (results != null) {
                while (results.next()) {
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
                            """, ProductID, ProductName, QuantityPerUnit, UnitPrice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

        try ( // open a connection to the database
              // use database URL to point to correct database
              Connection connection = basicDataSource.getConnection();
              PreparedStatement ps = connection.prepareStatement("SELECT ContactName, CompanyName, City, Country, Phone FROM customers");
              // execute query
              ResultSet results = ps.executeQuery();)
        {

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
        
        
    }

    private static void displayCategories() {

        // open the resources for connection, statement, and results
        try (Connection connection = basicDataSource.getConnection();
             // create a statement that uses the open connection to pass the query
             PreparedStatement ps = connection.prepareStatement("SELECT CategoryID, CategoryName FROM categories " +
                     "ORDER BY categoryID ASC");
             ResultSet results = ps.executeQuery();

        ) {

            while (results.next()) {
                int categoryID = results.getInt("CategoryID");
                String categoryName = results.getString("CategoryName");

                System.out.printf("""
                        CategoryID: %d
                        CategoryName: %s
                        -------------------------
                        """, categoryID, categoryName);
            }


            // prompt user to choose a categoryID to display its products
            PreparedStatement ps2 = connection.prepareStatement("SELECT CategoryID, ProductName, UnitPrice, UnitsInStock FROM products " +
                    "WHERE categoryID = ?");

            int userDefinedCategoryID = userDefineCategorySearch();
            ps2.setInt(1, userDefinedCategoryID);

            try (ResultSet results2 = ps2.executeQuery()) {
                while (results2.next()) {
                    int categoryID = results2.getInt("CategoryID");
                    String productName = results2.getString("ProductName");
                    int unitPrice = results2.getInt("UnitPrice");
                    int unitsInStock = results2.getInt("UnitsInStock");

                    System.out.printf("""
                            CategoryID: %d
                            Product Name: %s
                            Unit Price: %d
                            UnitsInStock: %d
                            -------------------------
                            """, userDefinedCategoryID, productName, unitPrice, unitsInStock);
                }
            } catch (SQLException sql) {
                sql.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        

    }
        
    
    private static int userDefineCategorySearch(){
        System.out.print("Choose a CategoryID to display its products: ");
        return s.nextInt();
    }
    
    
}