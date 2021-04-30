package com.example.instagram2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram2.Adapters.PostsAdapter;
import com.example.instagram2.Activities.LoginActivity;
import com.example.instagram2.Models.Post;
import com.example.instagram2.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static com.example.instagram2.Profile.getProfileUrl;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    private Button btnLogOut;
    private TextView tvUser;
    private EditText etAbout;
    private ImageView ivProfile;

    private ParseUser currentUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvProfilePosts);
        tvUser = view.findViewById(R.id.tvUser);
        etAbout = view.findViewById(R.id.etAbout);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser.logOut(); //Logs user out
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goToActivity(LoginActivity.class);
                Log.d(TAG,"Message ");
            }
        });

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
        SetVariables();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Function to go to a chosen activity
    private void goToActivity(Class targetClass) {
        Intent i = new Intent(getContext(), targetClass);
        startActivity(i);
        getActivity().finish();
    }

    private void SetVariables(){
        currentUser = ParseUser.getCurrentUser();
        tvUser.setText(currentUser.getString("username"));
        etAbout.setText(currentUser.getString("aboutMe"));
        Glide.with(getContext())
                .load(getProfileUrl(currentUser.getObjectId()))
                .circleCrop() // create an effect of a round profile picture
                .into(ivProfile);
    }
}
