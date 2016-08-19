package com.example.duclinh.appchat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterListMessageChat;
import com.example.duclinh.appchat.data.FormMessage;

import java.util.ArrayList;

/**
 * Created by haixo on 8/19/2016.
 */
public class ScreenChatFragment extends DialogFragment {
    private Context context;
    private RecyclerView listMessage;
    private ArrayList<FormMessage> listData;



    public ScreenChatFragment(){

    }
    public static ScreenChatFragment newInstance(String title){
        ScreenChatFragment screenChatFragment = new ScreenChatFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
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
        listData = new ArrayList<FormMessage>();
        AdapterListMessageChat adapterListMessageChat = new AdapterListMessageChat(listData);
        listMessage.setAdapter(adapterListMessageChat);
        adapterListMessageChat.notifyDataSetChanged();
    }


    private void controlView(View view) {
        listMessage = (RecyclerView) view.findViewById(R.id.fragment_screen_chat_listmessage);

    }
    private void controlEvent() {

    }
}
