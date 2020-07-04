package com.ldgr8.securechat;

import androidx.annotation.NonNull;

public class User {
    private String nickname,email;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return nickname+" "+email;
    }
}
