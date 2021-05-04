package com.example.instagram2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instagram2.R;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private Context context;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);

        setupToolbar();
    }

    private void setupToolbar(){
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(AccountSettingsActivity.class);
            }
        });

    }

    private void goToActivity(Class targetClass) {

        Intent i = new Intent(this, targetClass);
        startActivity(i);
        finish();
    }

}
