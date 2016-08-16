package com.example.duclinh.appchat.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.fragment.ForgetPasswordFragment;
import com.example.duclinh.appchat.orther.MyApplication;
import com.example.duclinh.appchat.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private AppCompatButton login;
    private TextView createAccount;
    private TextView forgetPassword;

    private Socket socket;

    public void signUp(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controllView();
        controllEvent();
        socket = MyApplication.getSocket();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==100 && resultCode ==100){
            account.setText(data.getStringExtra("account"));
            password.setText(data.getStringExtra("password"));
        }
    }

    private void controllEvent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textAccount = account.getText().toString();
                String textPassword = password.getText().toString();
                JSONObject jsonObject = new JSONObject();
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
                socket.emit("login", jsonObject);
                socket.once("resultLogin", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("account", account.getText().toString());
                intent.putExtra("password", password.getText().toString());
                startActivityForResult(intent, 100);
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

    private void controllView() {
        account = (EditText) findViewById(R.id.activity_login_account);
        password = (EditText) findViewById(R.id.activity_login_password);
        login = (AppCompatButton) findViewById(R.id.activity_login_login);
        createAccount = (TextView) findViewById(R.id.activity_login_createaccount);
        forgetPassword = (TextView) findViewById(R.id.activity_login_forgetpassword);
    }
}
