package com.example.chadmeetsstacey;

import java.util.ArrayList;
import java.util.List;

public class Match {
    // Instance variables
    private String hostEmail;
    private String matchedEmail;
    private String eventName;
    private String eventId;
    private List<Message> messages;

    // Constructors
    public Match() {
        // Needed for Firebase purposes
    }

    public Match(String hostEmail, String matchedEmail, String eventName, String eventId) {
        this.hostEmail = hostEmail;
        this.matchedEmail = matchedEmail;
        this.eventName = eventName;
        this.eventId = eventId;
        this.messages = new ArrayList<Message>();
    }

    // Getter methods
    public String getHostEmail() {
        return hostEmail;
    }

    public String getMatchedEmail() {
        return matchedEmail;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
