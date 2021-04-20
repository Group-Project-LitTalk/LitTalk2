package com.example.instagram2.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.instagram2.Adapters.PostsAdapter;
import com.example.instagram2.BookClasses.Book;
import com.example.instagram2.BookClasses.BookAdapter;
import com.example.instagram2.Post;
import com.example.instagram2.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Headers;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvSearch;
    EditText etSearch;
    protected BookAdapter adapter;
    List<Book> allBooks;
    public static final String URL = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rvSearch = findViewById(R.id.rvSearch);
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(URL+s, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "The JSONObject is as follows..." + json.toString());
                        try {
                            Log.d(TAG, json.jsonObject.getString("totalItems"));
                            Log.d(TAG, json.jsonObject.getJSONArray("items").getString(1));
                            allBooks.addAll(Book.fromJsonArray(json.jsonObject.getJSONArray("items")));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "Error Response", throwable);
                    }
                });
                filter(s.toString());
            }
        });

        allBooks = new ArrayList<>();
        adapter = new BookAdapter(SearchActivity.this, allBooks);

        rvSearch.setAdapter(adapter);

        rvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

    }
    private void filter(String text){
        ArrayList<Book> filteredList = new ArrayList<>();
        for (Book item : allBooks) {
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}