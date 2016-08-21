package com.example.duclinh.appchat.designnotifi;

/**
 * Created by haixo on 8/21/2016.
 */
public interface Observer {
    void notifiAllClient(String account , String message);
    void addClient(Client client);
}
