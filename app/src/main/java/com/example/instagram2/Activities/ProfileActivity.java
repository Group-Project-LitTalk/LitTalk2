package com.example.instagram2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram2.Adapters.PostsAdapter;
import com.example.instagram2.Models.Post;
import com.example.instagram2.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private Context context;

    private ProgressBar mProgressBar;


    private RecyclerView rvProfilePosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);

        setupToolbar();
    }

    private void setupToolbar(){
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(AccountSettingsActivity.class);
            }
        });

        rvProfilePosts = (RecyclerView) findViewById(R.id.rvProfilePosts);


        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(ProfileActivity.this, allPosts);

        rvProfilePosts.setAdapter(adapter);

        rvProfilePosts.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));

        queryPosts();

    }

    protected void queryPosts() {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);


        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername() + " ,Time: " + post.getCreatedAt());
                }

                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void goToActivity(Class targetClass) {

        Intent i = new Intent(this, targetClass);
        startActivity(i);
        finish();
    }

}
