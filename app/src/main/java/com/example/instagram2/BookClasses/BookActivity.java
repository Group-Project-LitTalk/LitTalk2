package com.example.instagram2.BookClasses;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.instagram2.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class BookActivity extends AppCompatActivity {

    public static final String TAG = "BookActivity";
    public static final String URL = "https://www.googleapis.com/books/v1/volumes/";

    TextView tvBookDescription;
    TextView tvRatingAlert;
    RatingBar ratingBar;
    ImageView ivCover;
    Button btnBookTitle;
    String book_id;

    double rating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tvBookDescription = findViewById(R.id.tvBookDescription);
        tvRatingAlert = findViewById(R.id.tvReviewAlert);
        ratingBar = findViewById(R.id.ratingBar);
        ivCover = findViewById(R.id.ivCover);
        btnBookTitle = findViewById(R.id.btnBookTitle);

        book_id = getIntent().getStringExtra("Book_ID");
        Log.d(TAG,"Book ID: " + book_id);
        Log.d(TAG, URL + book_id);

        loadDetails();
    }

    private void loadDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + book_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONObject volumeInfo = json.jsonObject.getJSONObject("volumeInfo");

                    // Sets the book image
                    Glide.with(BookActivity.this).load(volumeInfo.getJSONObject("imageLinks").getString("large"))
                            .override(ViewGroup.LayoutParams.MATCH_PARENT, 150).into(ivCover);
                    btnBookTitle.setText(volumeInfo.getString("title"));
                    // Retrieves the description and then clears it of characters defined in a list
                    String description = ReplaceChar(volumeInfo.getString("description"));
                    tvBookDescription.setText(description);
                    SetRating(volumeInfo);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Failed: " + throwable);
            }
        });
    }

    //Function used to replace specific characters
    private String ReplaceChar(String oldText) {
        String[] list = {"<b>", "<i>", "</i>", "<br>", "</b>"};
        String[] paraList = {"<p>", "</p>"};
        String newText = oldText;
        for (int i = 0; i < list.length; i++) {
            newText = newText.replaceAll(list[i], "");
        }
        for (int i = 0; i < paraList.length; i++) {
            newText = newText.replaceAll(paraList[i], "\r\n");
        }
        return newText;
    }

    private void SetRating(JSONObject info) {
        if (rating == 0) {
            try {
                rating = info.getDouble("averageRating");
                tvRatingAlert.setVisibility(View.GONE);
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(((float) rating));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG,"Something went wrong");
            }
        }
    }
}