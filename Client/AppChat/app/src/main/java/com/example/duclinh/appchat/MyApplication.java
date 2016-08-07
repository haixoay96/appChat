package com.example.duclinh.appchat;

import android.app.Application;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by duclinh on 07/08/2016.
 */
public class MyApplication extends Application  {
    private Socket socket = null;
    {
        try {
            socket = IO.socket("http://192.168.0.113:3000");
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*socket.connect();
        if(socket.connected()){
            Log.d("thanh", "thanh cong");
        }*/
    }
}
