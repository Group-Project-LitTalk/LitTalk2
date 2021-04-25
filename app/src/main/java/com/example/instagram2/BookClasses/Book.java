package com.example.instagram2.BookClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Book {

    String coverPath;
    String title;
    String BookId;

    public Book() {
    }

    public Book (JSONObject jsonObject) throws JSONException {
        JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");

        coverPath = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
        title = volumeInfo.getString("title");
        BookId = jsonObject.getString("id");
    }

    public static List<Book> fromJsonArray(JSONArray bookJsonArray) throws JSONException {
        Log.d("Book","JSON ARRAY RECEIVED: " + bookJsonArray.toString());
        List<Book> books = new ArrayList<>();
        for (int i = 0; i< bookJsonArray.length(); i++){
            books.add(new Book( bookJsonArray.getJSONObject(i)));
        }
        return books;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getTitle() {
        return title;
    }

    public String getBookId() {
        return BookId;
    }

}
