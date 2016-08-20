package com.example.duclinh.appchat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterListMessageChat;
import com.example.duclinh.appchat.data.FormMessage;
import com.example.duclinh.appchat.orther.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haixo on 8/19/2016.
 */
public class ScreenChatFragment extends DialogFragment {
    private Context context;
    private RecyclerView listMessage;
    private EditText message;
    private AppCompatButton send;
    private ArrayList<FormMessage> listData;
    private AdapterListMessageChat adapterListMessageChat;
    private RecyclerView.LayoutManager layoutManager;
    private String account;



    public ScreenChatFragment(){

    }
    public static ScreenChatFragment newInstance(String account){
        ScreenChatFragment screenChatFragment = new ScreenChatFragment();
        Bundle args = new Bundle();
        args.putString("account", account);
        screenChatFragment.setArguments(args);
        return screenChatFragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_chat,container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlView(view);
        controlLogic();
        controlEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
    }

    private void controlLogic() {
        account =getArguments().getString("account");
        getDialog().setTitle(account);
        listData = new ArrayList<FormMessage>();
        adapterListMessageChat = new AdapterListMessageChat(listData);
        layoutManager = new LinearLayoutManager(context);
        ((LinearLayoutManager)layoutManager).setReverseLayout(true);
        ((LinearLayoutManager)layoutManager).setStackFromEnd(true);
        listMessage.setLayoutManager(layoutManager);
        listMessage.setAdapter(adapterListMessageChat);
        adapterListMessageChat.notifyDataSetChanged();
    }


    private void controlView(View view) {
        listMessage = (RecyclerView) view.findViewById(R.id.fragment_screen_chat_listmessage);
        message = (EditText) view.findViewById(R.id.fragment_screen_chat_message);
        send = (AppCompatButton) view.findViewById(R.id.fragment_screen_chat_send);
    }
    private void controlEvent() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                String data = message.getText().toString();
                message.setText("");
                try {
                    object.put("account", account);
                    object.put("message", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication.socket.emit("sendMessage",object);
                listData.add(0,new FormMessage(MyApplication.HOST+"/data/avatar/defaultavatar.jpg",data));
                adapterListMessageChat.notifyDataSetChanged();
            }
        });

    }
}
