package com.example.instagram2;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0n5ShQ569HPtdz7O136A5XPcsNDlgSDIqOsmeagB")
                .clientKey("W4ovsQ7WSOpZvszlfqcGBRHk0EMigIaQSnkgdbF2")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
