package com.example.demo;

public class SharedService {
    private static SharedService instance;
    private Task currentTask;

    private SharedService() {}

    public static SharedService getInstance() {
        if (instance == null) {
            instance = new SharedService();
        }
        return instance;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
    }
}
