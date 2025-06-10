package com.pluralsight;


import java.sql.*;

public class Main {
    
    private static sqlConnectionInfo sqlConnectionInfo;
    public static void main(String[] args) {
        
        if (args.length != 3) {
            System.out.println(
                    "Application needs three arguments to run: " +
                            "java.com.pluralsight.Main <username> <password> <sqlURL>");
            System.exit(1);
        }
        
        sqlConnectionInfo = getSQLConnectionInfoFromArgs(args);
        
        try{
            displayCities(103);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        
        
    }
    
    public static sqlConnectionInfo getSQLConnectionInfoFromArgs(String[] args){
        // get username and password from the command line args
        String username = args[0];
        String password = args[1];

        String connectionString = args[2];
        
        return new sqlConnectionInfo(connectionString, username, password);
    }
    
    public static void displayCities(int countryID) throws ClassNotFoundException {
            // load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        
            
            // open a connection to the database
            // use database URL to point to correct database
        try (Connection connection = DriverManager.getConnection(
                sqlConnectionInfo.getConnectionString(),
                sqlConnectionInfo.getUsername(),
                sqlConnectionInfo.getPassword());
            // create statement
            // the statement is tied to the open connection
             PreparedStatement ps = connection.prepareStatement("SELECT city FROM city WHERE country_id = ?")) 
        {
            ps.setInt(1, countryID);
            
            // execute query
            try (ResultSet results = ps.executeQuery()) {
                // process the results
                while (results.next()) {
                    String city = results.getString("city");
                    System.out.println(city);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    
}