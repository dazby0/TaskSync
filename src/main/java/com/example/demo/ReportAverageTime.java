package com.example.demo;

public class ReportAverageTime {
    private String team;
    private String averageTimeSpent;

    public ReportAverageTime(String team, String averageTimeSpent) {
        this.team = team;
        this.averageTimeSpent = averageTimeSpent;
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
