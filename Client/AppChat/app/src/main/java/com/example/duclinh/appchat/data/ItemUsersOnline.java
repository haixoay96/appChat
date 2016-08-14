package com.example.duclinh.appchat.data;

/**
 * Created by haixo on 8/13/2016.
 */
public class ItemUsersOnline {
    private int avatar;
    private String nickName;
    private String status;

    public ItemUsersOnline(int avatar, String nickName, String status){
        this.avatar = avatar;
        this.nickName = nickName;
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
