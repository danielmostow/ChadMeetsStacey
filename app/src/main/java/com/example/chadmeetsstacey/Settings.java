package com.example.chadmeetsstacey;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    // Instance variables
    private String emailAddress;
    private int maxDistance;
    private List<Integer> ageRange;
    // 0 = male, 1 = female, 2 = other
    private int preferredGender;

    // Constructors
    public Settings() {
        // Need for Firebase purposes
    }

    public Settings(String emailAddress, int preferredGender) {
        this.emailAddress = emailAddress;
        this.preferredGender = preferredGender;
        //TODO: handle max distance and age range
        this.maxDistance = 10;
        this.ageRange = new ArrayList<Integer>();
        this.ageRange.add(18);
        this.ageRange.add(22);
    }

    // Getter methods

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public List<Integer> getAgeRange() {
        return ageRange;
    }

    public int getPreferredGender() {
        return preferredGender;
    }


}
