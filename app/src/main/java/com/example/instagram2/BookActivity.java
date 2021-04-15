package com.example.instagram2;

import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram2.BookClasses.Book;


import org.parceler.Parcels;

public class BookActivity extends AppCompatActivity {

    public static final String TAG = "BookActivity";

    TextView tvRating;
    TextView tvBookDescription;
    RatingBar ratingBar;
    ImageView ivCover;
    Button btnBookTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tvRating = findViewById(R.id.tvRating);
        tvBookDescription = findViewById(R.id.tvBookDescription);
        ratingBar = findViewById(R.id.ratingBar);
        ivCover = findViewById(R.id.ivCover);
        btnBookTitle = findViewById(R.id.btnBookTitle);

        Book book = Parcels.unwrap(getIntent().getParcelableExtra("book"));
    }


}
