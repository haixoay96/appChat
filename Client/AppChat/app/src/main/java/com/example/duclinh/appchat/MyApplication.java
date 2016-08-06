package com.example.duclinh.appchat;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by duclinh on 07/08/2016.
 */
public class MyApplication extends Application  {
    public static MyApplication myApplication;
    public static Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket = IO.socket("http://127.0.0.1:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        myApplication = this;
    }
}
