package com.example.chadmeetsstacey;

public class Settings {
    // Instance variables
    private String emailAddress;
    private int maxDistance;
    private int[] ageRange;
    // 0 = male, 1 = female, 2 = other
    private int preferredGender;
    //TODO: handle max distance and age range

    // Constructors

    public Settings() {
        // Need for Firebase purposes
    }

    public Settings(String emailAddress, int preferredGender) {
        this.emailAddress = emailAddress;
        this.preferredGender = preferredGender;
    }

    // Getter methods

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int[] getAgeRange() {
        return ageRange;
    }

    public int getPreferredGender() {
        return preferredGender;
    }


}
