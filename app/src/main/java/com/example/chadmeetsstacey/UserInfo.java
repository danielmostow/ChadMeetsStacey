package com.example.chadmeetsstacey;

public class UserInfo {
    // Instance variables
    private String emailAddress;
    private String name;
    private String greekOrg;
    private int age;
    private String grade;
    private String biography;
    // TODO: Add way to access pictures

    // Constructors
    public UserInfo() {
        // Need for Firebase purposes
    }

    public UserInfo(String emailAddress) {
        this.emailAddress = emailAddress;
        // TODO: Add initialization for other fields as they are added
    }

    // Getter methods

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getName() {
        return name;
    }

    public String getGreekOrg() {
        return greekOrg;
    }

    public int getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    public String getBiography() {
        return biography;
    }
}
