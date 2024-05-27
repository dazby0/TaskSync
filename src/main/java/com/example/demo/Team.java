package com.example.demo;

public class Team {
    String manager;
    String workers;
    String teamName;

    public Team(String manager, String workers, String teamName) {
        this.manager = manager;
        this.workers = workers;
        this.teamName = teamName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
