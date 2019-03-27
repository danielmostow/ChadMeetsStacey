package com.example.chadmeetsstacey;

import java.util.Date;
import java.util.List;

public class Message {
    // Instance variables
    private Date dateSent;
    private String messageBody;
    private String senderEmail;

    // Constructors
    public Message() {
        // Needed for Firebase purposes
    }

    public Message(String messageBody, String senderEmail) {
        // TODO: make sure date is correct
        this.dateSent = new Date();
        this.messageBody = messageBody;
        this.senderEmail = senderEmail;
    }

    // Getter methods
    public Date getDateSent() {
        return dateSent;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
}
