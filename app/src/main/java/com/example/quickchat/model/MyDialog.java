package com.example.quickchat.model;

public class MyDialog {
  private String message;
  private String muID;
  private long time;

    public MyDialog(String message, String muID, long time) {
        this.message = message;
        this.muID = muID;
        this.time = time;
    }
    public MyDialog(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMuID() {
        return muID;
    }

    public void setMuID(String muID) {
        this.muID = muID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
