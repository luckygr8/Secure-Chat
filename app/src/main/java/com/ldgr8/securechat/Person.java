package com.ldgr8.securechat;

public class Person {
    String nickname,message;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }
}
