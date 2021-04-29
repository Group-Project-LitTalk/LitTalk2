package com.example.instagram2;

import android.app.Application;

import com.example.instagram2.Models.Message;
import com.example.instagram2.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        ParseObject.registerSubclass(Message.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UNkn7D61Ce0erEA179pC4IrhDYN0caH1gDgP09aI")
                .clientKey("cn3B5dzZGjrKuzosUWZ0wvEnPvlnF8iYEaEw2e0H")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
