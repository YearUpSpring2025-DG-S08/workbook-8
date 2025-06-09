package com.pluralsight;


import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        // we created the Maven dependency in the pom.xml to utilize this object
        ObjectMapper on = new ObjectMapper();
    }
}