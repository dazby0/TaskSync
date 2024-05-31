package com.example.demo.Models;

public class ReportAverageTime {
    private String username;
    private String team;
    private String averageTimeSpent;

    public ReportAverageTime(String username, String team, String averageTimeSpent) {
        this.username = username;
        this.team = team;
        this.averageTimeSpent = averageTimeSpent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getAverageTimeSpent() {
        return averageTimeSpent;
    }

    public void setAverageTimeSpent(String averageTimeSpent) {
        this.averageTimeSpent = averageTimeSpent;
    }
}
