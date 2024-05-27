package com.example.demo;

public class Task {
    private String taskName;
    private String status;
    private String assignedTo;
    private String startDate;
    private String timeSpent;
    private String assignedBy;

    public Task(String taskName, String status, String assignedTo, String startDate, String timeSpent, String assignedBy) {
        this.taskName = taskName;
        this.status = status;
        this.assignedTo = assignedTo;
        this.startDate = startDate;
        this.timeSpent = timeSpent;
        this.assignedBy = assignedBy;
    }

    // Getters and setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getAssignedBy() { return assignedBy; }

    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
}
