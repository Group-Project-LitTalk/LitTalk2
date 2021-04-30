package com.example.instagram2.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram2.Adapters.CommentAdapter;
import com.example.instagram2.Models.Comment;
import com.example.instagram2.Models.Message;
import com.example.instagram2.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommentActivity extends AppCompatActivity {

    static final String TAG = "CommentActivity";

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";


    RecyclerView rvComment;
    ArrayList <Comment> mComments;
    CommentAdapter mAdapter;
    TextView tvPost;

    boolean mFirstLoad;

    EditText etComment;
    ImageButton btSend;

    int contextMenuIndexClicked = -1;
    boolean isEditMode = false;
    Message editMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        refreshComments();

        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery <Comment> parseQuery = ParseQuery.getQuery(Comment.class);

        SubscriptionHandling<Comment> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<Comment>() {
            @Override
            public void onEvent(ParseQuery<Comment> query, Comment object) {
                mComments.add(0, object);
                Log.d(TAG,"onEvent");
                // RecyclerView updates need to be run on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        rvComment.scrollToPosition(0);
                    }
                });
            }
        });

    }

    void startWithCurrentUser() {
        setupCommentPosting();
    }

    void setupCommentPosting() {
        // Find the text field and button
        etComment = (EditText) findViewById(R.id.etComment);
        btSend = (ImageButton) findViewById(R.id.btSend);
        rvComment = (RecyclerView) findViewById(R.id.rvComment);

        tvPost = (TextView) findViewById(R.id.tvPost);

        mComments = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();

        mAdapter = new CommentAdapter(CommentActivity.this, userId, mComments);
        rvComment.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvComment.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etComment.getText().toString();
                //ParseObject message = ParseObject.create("Message");
                //message.put(Message.USER_ID_KEY, userId);
                //message.put(Message.BODY_KEY, data);
                // Using new `Message` Parse-backed model now
                Comment comment = new Comment();
                comment.setBody(data);
                comment.setUserId(ParseUser.getCurrentUser().getObjectId());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(CommentActivity.this, "Successfully created comment on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshComments();
                    }
                });
                etComment.setText(null);
            }
        });
    }

    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    void refreshComments() {
        // Construct query to execute
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse a
        // synchronously
        // This is equivalent to a SELECT query with SQL

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e == null) {
                    mComments.clear();
                    mComments.addAll(objects);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvComment.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("comment", "Error Loading Comments" + e);
                }
            }
        });

    }

    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    Handler myHandler = new Handler();

    Runnable mRefreshCommentsRunnable = new Runnable() {
        @Override
        public void run() {
            refreshComments();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Only start checking for new messages when the app becomes active in foreground
        //myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        // Stop background task from refreshing messages, to avoid unnecessary traffic & battery drain
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

}