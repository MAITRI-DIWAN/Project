package com.example.newproject;

public class Assignment {
    private int id;
    private int teacherId;

    private String name;

    private int batchId;
    private String deadline;
    private int isActive;
    private String username;

    // Constructor
    public Assignment(int id, int teacherId, String name, int batchId, String deadline, int isActive, String username) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.batchId = batchId;
        this.deadline = deadline;
        this.isActive = isActive;
        this.username = username;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getName(){
        return name;
    }
    public void setName(){
        this.name = name;
    }



    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getTeacherUsername() {
        return username;
    }

    public void setTeacherUsername(String username) {
        this.username = username;
    }
}

