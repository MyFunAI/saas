package com.iaskdata.test;

import java.util.Arrays;

import org.junit.Test;

import com.iaskdata.util.Constant;

public class BinarySearchTest {

    public static final String rest[] = Constant.rest;
    
    @Test
    public void install() {
        System.out.println("install: " + Arrays.binarySearch(rest, "install"));
    }
    
    @Test
    public void startup() {
        System.out.println("startup: " + Arrays.binarySearch(rest, "startup"));
    }
    
    @Test
    public void register() {
        System.out.println("register: " + Arrays.binarySearch(rest, "register"));
    }
    
    @Test
    public void loggedin() {
        System.out.println("loggedin: " + Arrays.binarySearch(rest, "loggedin"));
    }
    
    @Test
    public void payment() {
        System.out.println("payment: " + Arrays.binarySearch(rest, "payment"));
    }
    
    @Test
    public void economy() {
        System.out.println("economy: " + Arrays.binarySearch(rest, "economy"));
    }
    
    @Test
    public void quest() {
        System.out.println("quest: " + Arrays.binarySearch(rest, "quest"));
    }
    
    @Test
    public void event() {
        System.out.println("event: " + Arrays.binarySearch(rest, "event"));
    }
    
    @Test
    public void heartbeat() {
        System.out.println("heartbeat: " + Arrays.binarySearch(rest, "heartbeat"));
    }
    
    @Test
    public void profile() {
        System.out.println("profile: " + Arrays.binarySearch(rest, "profile"));
    }
}
