package com.example.instagram2.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Map;

@ParseClassName("Post")
public class Post extends ParseObject  {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_BOOK_ID = "BookID";
    public static final String KEY_BOOK_TITLE = "BookTitle";


    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

    public String getKeyBookId(){
        return getString(KEY_BOOK_ID);
    }
    public void setBookId(String bookId)
    {
        put(KEY_BOOK_ID, bookId);
    }

    public String getKeyBookTitle()
    {
        return getString(KEY_BOOK_TITLE);
    }
    public void setBookTitle(String bookTitle)
    {
        put(KEY_BOOK_TITLE, bookTitle);
    }

    public ParseFile getImage()
    {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile)
    {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }

    public String getUsername(){return getString(KEY_USER);}

    @Override
    public String toString() {
        return " " + KEY_USER;
    }
}
