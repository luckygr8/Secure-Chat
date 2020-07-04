package com.ldgr8.securechat;

public class Room {
    private String roomname,roompass,roomid;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getRoompass() {
        return roompass;
    }

    public void setRoompass(String roompass) {
        this.roompass = roompass;
    }

    public Room(String roomname, String roompass) {
        this.roomname = roomname;
        this.roompass = roompass;
    }
}
