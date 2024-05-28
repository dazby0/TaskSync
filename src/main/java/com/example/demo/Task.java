package com.example.demo;

public class Task {
    private String taskName;
    private String status;
    private String assignedTo;
    private String assignedDate;
    private String timeSpent;
    private String assignedBy;
    private String finishTime;

    public Task(String taskName, String status, String assignedTo, String assignedDate, String timeSpent, String assignedBy, String finishTime) {
        this.taskName = taskName;
        this.status = status;
        this.assignedTo = assignedTo;
        this.assignedDate = assignedDate;
        this.timeSpent = timeSpent;
        this.assignedBy = assignedBy;
        this.finishTime = finishTime;
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

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getAssignedBy() { return assignedBy; }

    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }

    public String getFinishTime() { return finishTime; }

    public void setFinishTime(String finishTime) { this.finishTime = finishTime; }
}
