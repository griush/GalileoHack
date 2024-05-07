package com.example.galileomastermindboilerplate;

public class ServerHostname {
  
    private static final String IP = "192.168.1.100";
    public static final String ENDPOINT_SATELLITE = "http://" + IP + ":4321/fetch";
    public static final String ENDPOINT_LOCATION = "http://" + IP + ":4321/ephemeris";
}
