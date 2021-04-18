package com.example.instagram2.BookClasses;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.instagram2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class BookActivity extends AppCompatActivity {

    public static final String TAG = "BookActivity";
    public static final String URL = "https://www.googleapis.com/books/v1/volumes/x9x_DwAAQBAJ";

    TextView tvBookDescription;
    //RatingBar ratingBar;
    ImageView ivCover;
    Button btnBookTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tvBookDescription = findViewById(R.id.tvBookDescription);
        //ratingBar = findViewById(R.id.ratingBar);
        ivCover = findViewById(R.id.ivCover);
        btnBookTitle = findViewById(R.id.btnBookTitle);

        //tvRating.setText(" " + book.getRating());
        //tvBookDescription.setText(book.getDescription());
        //ratingBar.setRating((float)book.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONObject items = json.jsonObject.getJSONObject("volumeInfo");
                    Log.d(TAG,json.toString());
                    Log.d(TAG, String.valueOf(items));
                    btnBookTitle.setText(items.getString("title"));
                    tvBookDescription.setText(items.getString("description"));
                    Glide.with(BookActivity.this).load(items.getJSONObject("imageLinks").getString("large")).override(ViewGroup.LayoutParams.MATCH_PARENT,250).into(ivCover);
                }
                catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON", e);
                }
                Log.d(TAG, "yay");
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "nay");
            }
        });
    }
}