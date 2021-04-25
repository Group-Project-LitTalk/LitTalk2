package com.example.instagram2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram2.Main.MainActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvLogin;
    private Button btnRegister;
    //activity_main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signUp button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                registerUser(username, password);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Message that it's working");
                Toast.makeText(RegisterActivity.this, "Chill, that ain't in yet!", Toast.LENGTH_SHORT).show();
                goToActivity(LoginActivity.class);
            }
        });

    }

    private void registerUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        Log.i(TAG, "Attempting to signUp user" + username);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    loginUser(username,password);
                } else {
                    Log.e(TAG, "sign up failed" + e);
                }
            }
        });
    }
    private void loginUser(String username, String password) {

        Log.i(TAG, "Attempting to login user" + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "issue with login" + e);
                    return;
                }
                goToActivity(MainActivity.class);
                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToActivity(Class targetClass) {

        Intent i = new Intent(this, targetClass);
        startActivity(i);
        finish();
    }
}