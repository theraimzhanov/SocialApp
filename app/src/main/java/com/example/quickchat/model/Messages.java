package com.example.quickchat.model;

public class Messages {
    private String message;
     private String id;

    public Messages(){
     }
    public Messages(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
