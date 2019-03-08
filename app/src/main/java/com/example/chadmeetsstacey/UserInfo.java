package com.example.chadmeetsstacey;

public class UserInfo {
    // Instance variables
    private String emailAddress;
    private String firstName;
    private String greekOrg;
    private int age;
    private String grade;
    private String biography;
    // TODO: Add way to access pictures

    // Constructors
    public UserInfo() {
        // Need for Firebase purposes
    }

    public UserInfo(String emailAddress, String firstName, String greekOrg, int age, String grade) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.greekOrg = greekOrg;
        this.age = age;
        this.grade = grade;
    }

    // Getter methods

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
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
