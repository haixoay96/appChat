package com.example.duclinh.appchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.fragment.ForgetPasswordFragment;
import com.example.duclinh.appchat.fragment.SignupFragment;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private AppCompatButton login;
    private TextView createAccount;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlView();
        controlLogic();
        controlEvent();
    }
    private void controlLogic() {

    }

    private void controlEvent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyApplication.socket.connected()){
                    MyApplication.socket.connect();
                }
                final String textAccount = account.getText().toString();
                final String textPassword = password.getText().toString();
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("account", textAccount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("password", textPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication.socket.emit("login", jsonObject);
                MyApplication.socket.once("resultLogin", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = (JSONObject) args[0];
                                    int statusCode = 0;
                                    statusCode = jsonObject.getInt("status");
                                    if(statusCode == 100){
                                        JSONArray list = null;
                                        list = jsonObject.getJSONArray("listUsersOnline");
                                        Intent intent = new Intent(MainActivity.this, UsersOnlineActivity.class);
                                        intent.putExtra("listUsersOnline", list.toString());
                                        intent.putExtra("account",textAccount);
                                        intent.putExtra("password",textPassword);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SignupFragment fragment = SignupFragment.newInstance("Create account");
                fragment.show(fragmentManager, "fragment");
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ForgetPasswordFragment fragment = ForgetPasswordFragment.newInstance("Rest password");
                fragment.show(fragmentManager, "fragment");
            }
        });


    }

    private void controlView() {
        account = (EditText) findViewById(R.id.activity_login_account);
        password = (EditText) findViewById(R.id.activity_login_password);
        login = (AppCompatButton) findViewById(R.id.activity_login_login);
        createAccount = (TextView) findViewById(R.id.activity_login_createaccount);
        forgetPassword = (TextView) findViewById(R.id.activity_login_forgetpassword);
    }
}
