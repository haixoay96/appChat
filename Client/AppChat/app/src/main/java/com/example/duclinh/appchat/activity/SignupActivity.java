package com.example.duclinh.appchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duclinh.appchat.MyApplication;
import com.example.duclinh.appchat.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private EditText rePassword;
    private AppCompatButton createAccount;
    private Socket socket = MyApplication.getSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        controlView();
        controlEvent();
        String textAccount = getIntent().getStringExtra("account");
        account.setText(textAccount);
    }

    private void controlView() {
        account = (EditText) findViewById(R.id.activity_signup_account);
        password = (EditText) findViewById(R.id.activity_signup_password);
        rePassword = (EditText) findViewById(R.id.activity_signup_repassword);
        createAccount = (AppCompatButton) findViewById(R.id.activity_signup_createaccount);
    }

    private void controlEvent() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String textAccoun = account.getText().toString();
                final String textPassword = password.getText().toString();
                String textRePassword = rePassword.getText().toString();
                if(textPassword.equals("")||textPassword.equals("")||textRePassword.equals("")){
                    Toast.makeText(SignupActivity.this, "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!textPassword.equals(textRePassword)){
                    Toast.makeText(SignupActivity.this, "Xác nhận mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("account", textAccoun);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("password", textPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("signUp", jsonObject);
                socket.on("resultSignUp", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        SignupActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject data = (JSONObject) args[0];
                                int errorCode = 0;
                                try {
                                    errorCode = data.getInt("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(errorCode == 100){
                                    // when sigup successfull
                                    Intent intent = getIntent();
                                    intent.putExtra("account", textAccoun);
                                    intent.putExtra("password", textPassword);
                                    SignupActivity.this.setResult(100,intent);
                                    finish();
                                }
                                else if (errorCode == 101){
                                    /// when account already exist
                                }
                                else if(errorCode == 102){
                                    /// when email error
                                }
                                else {
                                    // when app error
                                    Toast.makeText(SignupActivity.this, "Application error! Please feedback!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


            }
        });
    }
}
