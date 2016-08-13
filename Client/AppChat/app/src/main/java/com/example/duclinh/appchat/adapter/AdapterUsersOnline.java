package com.example.duclinh.appchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.duclinh.appchat.data.ItemUsersOnline;

import java.util.ArrayList;

/**
 * Created by haixo on 8/13/2016.
 */
public class AdapterUsersOnline extends RecyclerView.Adapter<AdapterUsersOnline.MyViewHolder> {
    private ArrayList<ItemUsersOnline> listUsersOnlines;

    public AdapterUsersOnline(ArrayList<ItemUsersOnline> listUsersOnlines) {
        this.listUsersOnlines = listUsersOnlines;
    }

    public class MyViewHolder(View view){

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
