package com.example.duclinh.appchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterUsersOnline;

public class UsersOnlineActivity extends AppCompatActivity {
    private RecyclerView listUsers;
    private AdapterUsersOnline adapterUsersOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);
        controlView();
        controlEvent();
    }

    private void controlEvent() {

    }

    private void controlView() {
        listUsers = (RecyclerView) findViewById(R.id.activity_users_online_listusers);
    }
}
