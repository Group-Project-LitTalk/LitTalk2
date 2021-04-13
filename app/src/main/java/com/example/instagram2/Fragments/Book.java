package com.example.instagram2.Fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Book {

    String posterPath;
    String title;
    String overview;

    public Book (JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    public static List<Book> fromJsonArray(JSONArray bookJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i< bookJsonArray.length(); i++){
            books.add(new Book( bookJsonArray.getJSONObject(i)));
        }
        return books;
    }

    public String getPosterPath() {
        return String.format( "https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
