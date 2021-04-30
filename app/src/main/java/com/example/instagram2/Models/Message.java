package com.example.instagram2.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "user";
    public static final String BODY_KEY = "body";
    public static final String POST_ID_KEY = "PointerPost";
    public static final String USERNAME = "username";

    public String getBody() {
        return getString(BODY_KEY);
    }

    private ParseUser getUser()
    {
        return getParseUser(USER_ID_KEY);
    }

    public String getUserId() {
        return getUser().getObjectId();
    }

    public void setUser(ParseUser user)
    {
        put(USER_ID_KEY, user);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public String getPostId() { return getString(POST_ID_KEY); }

    public void setPostId(String PostId) { put(POST_ID_KEY, PostId); }

    public String getUsername(){return getString(USERNAME);}

    public void setUsername(String username) {put(USERNAME, username); }
}
