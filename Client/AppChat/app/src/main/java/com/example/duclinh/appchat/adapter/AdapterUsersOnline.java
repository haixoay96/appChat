package com.example.duclinh.appchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.data.ItemUsersOnline;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haixo on 8/13/2016.
 */
public class AdapterUsersOnline extends RecyclerView.Adapter<AdapterUsersOnline.MyViewHolder> {
    private ArrayList<ItemUsersOnline> listUsersOnlines;
    private ViewGroup parent;

    public AdapterUsersOnline(ArrayList<ItemUsersOnline> listUsersOnlines) {
        this.listUsersOnlines = listUsersOnlines;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView avatar;
        private TextView nickName;
        private TextView status;

        public MyViewHolder(View view) {
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.item_users_online_avatar);
            nickName = (TextView) view.findViewById(R.id.item_users_online_nickname);
            status = (TextView) view.findViewById(R.id.item_users_online_status);
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }

        public CircleImageView getAvatar() {
            return avatar;
        }

        public void setAvatar(CircleImageView avatar) {
            this.avatar = avatar;
        }

        public TextView getNickName() {
            return nickName;
        }

        public void setNickName(TextView nickName) {
            this.nickName = nickName;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_online, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemUsersOnline itemUsersOnline = listUsersOnlines.get(position);
        Picasso.with(parent.getContext()).load(itemUsersOnline.getAvatar()).resize(200,200).into(holder.getAvatar());
        holder.getNickName().setText(itemUsersOnline.getNickName());
        holder.getStatus().setText(itemUsersOnline.getStatus());
    }

    @Override
    public int getItemCount() {
        return listUsersOnlines.size();
    }


}
