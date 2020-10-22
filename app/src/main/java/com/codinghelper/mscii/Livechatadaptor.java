package com.codinghelper.mscii;

public class Livechatadaptor {
    private String message,senderID;

    public Livechatadaptor() {
    }

    public Livechatadaptor(String message, String senderID) {
        this.message = message;
        this.senderID = senderID;
    }

    public String getmessage() {
        return message;
    }

    public String getsenderID() {
        return senderID;
    }
}
