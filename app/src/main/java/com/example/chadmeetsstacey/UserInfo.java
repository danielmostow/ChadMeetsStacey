package com.example.chadmeetsstacey;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    // Instance variables
    private String emailAddress;
    private String firstName;
    private String greekOrg;
    private int age;
    // 0 = male, 1 = female, 2 = other
    private int gender;
    private String grade;
    private String biography;
    private boolean hasProfilePic;
    private List<String> eventsSwipedOn;

    // Constructors
    public UserInfo() {
        // Need for Firebase purposes
    }

    public UserInfo(String emailAddress, String firstName, String greekOrg, int age, int gender, String grade) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.greekOrg = greekOrg;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
        this.biography = "";
        this.hasProfilePic = false;
        this.eventsSwipedOn = new ArrayList<String>();
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

    public int getGender() { return gender; }

    public String getGrade() {
        return grade;
    }

    public String getBiography() {
        return biography;
    }

    public boolean getHasProfilePic() {
        return hasProfilePic;
    }

    public List<String> getEventsSwipedOn() {
        return eventsSwipedOn;
    }

    // Provide means to append to eventsSwipedOn
    public void addEventSwipeOn(String eventId) {
        this.eventsSwipedOn.add(eventId);
    }
}
