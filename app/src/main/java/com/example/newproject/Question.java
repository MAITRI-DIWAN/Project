package com.example.newproject;

public class Question {
    private int id;
    private String question;
    private String answer;
    private int isActive;

    // Constructor
    public Question(int id, String question, String answer, int isActive) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.isActive = isActive;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return question;
    }

    public void setQuestionText(String questionText) {
        this.question = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
