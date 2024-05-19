package com.example.demo;

public class Task {
    private String username;
    private String role;

    public Task(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
