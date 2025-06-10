package com.pluralsight;


import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;

public class Main {
    
    private static BasicDataSource basicDataSource;
    public static void main(String[] args) {
        
        if (args.length != 3) {
            System.out.println(
                    "Application needs three arguments to run: " +
                            "java.com.pluralsight.Main <username> <password> <sqlURL>");
            System.exit(1);
        }
        
        basicDataSource = getBasicDataSourceFromArgs(args);
        
        try{
            displayUserDefinedCities(103);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        
        
    }
    
    // this method loads the driver
    public static BasicDataSource getBasicDataSourceFromArgs(String[] args){
        // get username and password from the command line args
        String username = args[0];
        String password = args[1];
        String connectionString = args[2];
        
        BasicDataSource result = new BasicDataSource();
        result.setUsername(username);
        result.setPassword(password);
        result.setUrl(connectionString);

        
        return result;
    }
    
    public static void displayUserDefinedCities(int countryID) {
            // open a connection to the database
        try (Connection connection = basicDataSource.getConnection();
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
    
    public static void displayAllCities() {
        
        try(Connection connection = basicDataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT city FROM city");
        ResultSet results = ps.executeQuery();
        )
        {
            while(results.next()){
                String city = results.getString("city");
                System.out.println(city);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}