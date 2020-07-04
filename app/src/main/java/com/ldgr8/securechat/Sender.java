package com.ldgr8.securechat;

import androidx.annotation.NonNull;

public class Sender {
    private String sender_name;
    private String sender_message;
    private String sender_time;


    public Sender(String sender_name, String sender_message,String sender_time) {
        this.sender_name = sender_name;
        this.sender_message = sender_message;
        this.sender_time=sender_time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getSender_message() {
        return sender_message;
    }

    public String getSender_time() {
        return sender_time;
    }

    @NonNull
    @Override
    public String toString() {
        return sender_message;
    }
}
