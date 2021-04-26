package com.example.instagram2.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT = "comment";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";

    private Object timestamp;

    public static String getKeyComment() {
        return KEY_COMMENT;
    }

    public void setKeyComment(String description)
    {
        put(KEY_COMMENT, description);
    }

    public static String getKeyUser() {
        return KEY_USER;
    }

    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }

    public static String getKeyCreatedKey() {
        return KEY_CREATED_KEY;
    }
}
