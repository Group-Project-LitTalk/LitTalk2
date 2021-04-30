package com.example.instagram2;

import com.example.instagram2.Models.Post;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile {
    public static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    public String getDate(Post post) {
        Date date = post.getCreatedAt();
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        String time = TimeFormatter.getTimeDifference(df.format(date));
        return time;
    }
}
