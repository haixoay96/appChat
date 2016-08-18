package com.example.duclinh.appchat.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haixo on 8/16/2016.
 */
public class ForgetPasswordFragment extends DialogFragment {
    private EditText account;
    private AppCompatButton resetpassword;
    private Socket socket;
    private Context context;

    public ForgetPasswordFragment(){
    }

    public static ForgetPasswordFragment newInstance(String title){
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        socket = MyApplication.getSocket();
        controlView(view);
        controlEvent();

    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

    }
    private void controlView(View view) {
        account = (EditText) view.findViewById(R.id.fragmnet_forget_password_account);
        resetpassword = (AppCompatButton) view.findViewById(R.id.fragmnet_forget_password_resetpassword);

    }
    private void controlEvent() {
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("account", account.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("forgetPassword",object );
                socket.once("resultForgetPassword", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, args[0]+"", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
               // dismiss();
            }
        });
    }




}
