package com.example.duclinh.appchat.designnotifi;

import java.util.ArrayList;

/**
 * Created by haixo on 8/21/2016.
 */
public class CenterManagerMessage implements Observer {
    private ArrayList<Client> listClient;
    public CenterManagerMessage(){
        listClient = new ArrayList<Client>();
    }
    @Override
    public void addClient(Client client){
        listClient.add(client);
    }
    @Override
    public void notifiAllClient(String account, String message) {
        for (Client client:listClient) {
            client.update(account, message);
        }
    }
}
