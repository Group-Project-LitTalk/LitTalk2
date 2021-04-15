package com.example.instagram2.BookClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Book {

    String bookId;
    String posterPath;
    String title;
    String tvBookDescription;
    double rating;

    public Book() {
    }

    public Book (JSONObject jsonObject) throws JSONException {
        bookId = jsonObject.getString("id");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        tvBookDescription = jsonObject.getString("overview");
        rating = jsonObject.getDouble("rating");
    }

    public static List<Book> fromJsonArray(JSONArray bookJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i< bookJsonArray.length(); i++){
            books.add(new Book( bookJsonArray.getJSONObject(i)));
        }
        return books;
    }

    public String getBookId(){return bookId;}

    public String getPosterPath() {
        return String.format( "https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getTvBookDescription() {
        return tvBookDescription;
    }

    public double getRating(){return rating;}

}
