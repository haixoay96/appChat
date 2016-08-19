package com.example.duclinh.appchat.data;

/**
 * Created by haixo on 8/19/2016.
 */
public class FormMessage {
    private String avatar;
    private String message;

    public FormMessage(String avatar, String message) {
        this.message = message;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
