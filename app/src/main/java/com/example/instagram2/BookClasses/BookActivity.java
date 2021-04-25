package com.example.instagram2.BookClasses;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

    float rating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tvBookDescription = findViewById(R.id.tvBookDescription);
        tvRatingAlert = findViewById(R.id.tvRatingAlert);
        ratingBar = findViewById(R.id.ratingBar);
        ivCover = findViewById(R.id.ivCover);
        btnBookTitle = findViewById(R.id.btnBookTitle);

        book_id = getIntent().getStringExtra("Book_ID");
        Log.d(TAG,URL + book_id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + book_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {

                    JSONObject volumeInfo = json.jsonObject.getJSONObject("volumeInfo");
                    Log.d(TAG,"Volume info: "+ volumeInfo.toString());
                    btnBookTitle.setText(volumeInfo.getString("title"));
                    String description = replaceCharacters(volumeInfo.getString("description"));
                    tvBookDescription.setText(description);
                    Glide.with(BookActivity.this).load(volumeInfo.getJSONObject("imageLinks").getString("large"))
                            .override(ViewGroup.LayoutParams.MATCH_PARENT,150).into(ivCover);
                    SetRating(volumeInfo);
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

    private void SetRating(JSONObject info){
        try {
            rating = (float) info.getDouble("averageRating");
            ratingBar.setRating(rating);
            ratingBar.setVisibility(View.VISIBLE);
            tvRatingAlert.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String replaceCharacters (String oldText){
        String [] charList = {"<b>","<i>","</i>","<br>","</b>"};
        String [] paragraph = {"<p>","</p>"};
        String newText = oldText;
        for (int i = 0; i < charList.length; i++){
            Log.d(TAG,"New Text: " + newText);
            newText = newText.replaceAll(charList[i],"");
        }
        for (int i = 0; i < paragraph.length; i++){
            newText = newText.replaceAll(paragraph[i],"\r\n");
        }
        Log.d(TAG,newText);
        return newText;
    }
}