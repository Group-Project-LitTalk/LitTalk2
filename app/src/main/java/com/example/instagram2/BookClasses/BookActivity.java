package com.example.instagram2.BookClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.instagram2.R;

public class BookActivity extends AppCompatActivity {

    private TextView tv;
    private Button btnTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        btnTitle = findViewById(R.id.btnBookTitle);
    }
}