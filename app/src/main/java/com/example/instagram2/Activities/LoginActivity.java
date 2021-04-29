package com.example.instagram2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram2.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvRegister;
    private Button btnLogin;
//activity_main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null)
        {
            goToActivity(MainActivity.class);
        }


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnRegister);
        tvRegister = findViewById(R.id.tvLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Message that it's working");
                Toast.makeText(LoginActivity.this, "Chill, that ain't in yet!", Toast.LENGTH_SHORT).show();
                goToActivity(RegisterActivity.class);
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
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToActivity(Class targetClass) {

        Intent i = new Intent(this, targetClass);
        startActivity(i);
        finish();
    }
}