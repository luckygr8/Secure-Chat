package com.ldgr8.securechat;

import androidx.annotation.NonNull;

public class You {
    private String your_message;
    private String your_time;

    public You(String your_message, String your_time) {
        this.your_message = your_message;
        this.your_time = your_time;
    }

    public String getYour_message() {
        return your_message;
    }

    public String getYour_time() {
        return your_time;
    }

    @NonNull
    @Override
    public String toString() {
        return your_message;
    }
}
