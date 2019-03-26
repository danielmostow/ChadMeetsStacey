package com.example.chadmeetsstacey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserEvent {
    // Instance variables
    private String creatingUser;
    private String greekOrg;
    private String eventName;
    private String date;
    private String time;
    private String description;
    private String location;
    private List<String> potentialMatches;
    private List<String> declinedMatches;
    private boolean matchConfirmed;

    // Constructors
    public UserEvent() {
        // Need for Firebase purposes
        //TODO: Maybe?
    }

    public UserEvent(String en, String d, String t, String des, String loc) {
        // Use to get user info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        creatingUser = user.getEmail();
        greekOrg = null;    //TODO: Change when greek org functionality is added
        eventName = en;
        date = d;
        time = t;
        description = des;
        location = loc;
        potentialMatches = new ArrayList<String>(); // Initially are no matches for user
        declinedMatches = new ArrayList<String>();
        matchConfirmed = false;
    }

    // Getter methods
    public String getCreatingUser() {
        return creatingUser;
    }

    public String getGreekOrg() {
        return greekOrg;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getPotentialMatches() {
        return potentialMatches;
    }

    public List<String> getDeclinedMatches() {
        return declinedMatches;
    }

    public boolean getMatchConfirmed() {
        return matchConfirmed;
    }

    // Provide means to append to potentialMatches and declinedMatches
    public void addPotentialMatch(String userEmail) {
        this.potentialMatches.add(userEmail);
    }

    public void addDeclinedMatch(String userEmail) {
        this.declinedMatches.add(userEmail);
    }

}
