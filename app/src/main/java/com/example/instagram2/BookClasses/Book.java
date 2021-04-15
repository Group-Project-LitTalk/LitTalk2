package com.example.instagram2.BookClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Book {

    String coverPath;
    String title;
    String description;
    //double rating;

    public Book() {
    }

    public Book (JSONObject jsonObject) throws JSONException {
        coverPath = jsonObject.getString("thumbnail");
        title = jsonObject.getString("title");
        description = jsonObject.getString("description");
        //rating = jsonObject.getDouble("rating");
    }

    public static List<Book> fromJsonArray(JSONArray bookJsonArray) throws JSONException {
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

    public String getDescription() {
        return description;
    }

    //public double getRating(){return rating;}

}
