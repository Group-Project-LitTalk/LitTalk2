package com.example.instagram2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram2.Adapters.ChatAdapter;
import com.example.instagram2.Main.MainActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();

    Activity activity = this;

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    String postId;

    boolean mFirstLoad;

    EditText etMessage;
    ImageButton btSend;
    TextView tvPost;
    TextView tvUsername;
    TextView tvTime;
    ImageView backArrow;
    ImageView ivPostPic;

    int contextMenuIndexClicked = -1;
    boolean isEditMode = false;
    Message editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // User login
        postId = getIntent().getStringExtra("user");

        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        refreshMessages();

        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);

        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery <Message> query, Message object) {
                        mMessages.add(0, object);

                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                rvChat.scrollToPosition(0);
                            }
                        });
                    }
                });

    }

    void startWithCurrentUser() {

        tvPost = (TextView) findViewById(R.id.tvPost);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvTime = (TextView) findViewById(R.id.tvTime);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        ivPostPic = (ImageView) findViewById(R.id.ivPostPic);

        gettingPostInfo(postId, tvPost, tvUsername, tvTime, ivPostPic);
        setupMessagePosting();
    }

    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (ImageButton) findViewById(R.id.btSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);

        mMessages = new ArrayList<>();

        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();

        mAdapter = new ChatAdapter(ChatActivity.this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                //ParseObject message = ParseObject.create("Message");
                //message.put(Message.USER_ID_KEY, userId);
                //message.put(Message.BODY_KEY, data);
                // Using new `Message` Parse-backed model now
                Message message = new Message();
                message.setBody(data);

                //tvUsername.setText(post.getUser().getUsername());

                message.setPostId(postId);

                //message.setUserId(ParseUser.getCurrentUser().getObjectId());

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });

    }

    public void attemptPostInfo(Message message)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        query.getInBackground(postId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    ParseUser user = object.getParseUser(postId);
                    message.setPostId(object.getObjectId());

                } else {
                    Log.d(TAG, "Something is wrong");
                    // something went wrong
                }
            }
        });
    }

    public void gettingPostInfo(String objectId, TextView tvPost, TextView tvUsername, TextView tvTime, ImageView ivPostPic)
    {

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToActivity(MainActivity.class);
                Log.d(TAG, "It worked");
            }
        });

        tvUsername.setText(getIntent().getStringExtra("username"));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        query.getInBackground(postId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    ParseUser user = object.getParseUser(postId);

                   // Log.d("ChatActivity", object.getString("description"));
                   // Log.d("ChatActivity", tvUsername.toString());

                    Log.d(TAG, "Post description: " + object.getString("description"));
                    tvPost.setText(object.getString("description"));

                    Log.d(TAG, "Post Date: " + getDate(object));
                    tvTime.setText(getDate(object));

                    if(getIntent().getStringExtra("imageP") != null)
                    {
                        Glide.with(activity).load(getIntent().getStringExtra("imageP"))
                                .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).fitCenter().into(ivPostPic);
                    }


                } else {
                    Log.d(TAG, "Something is wrong");
                    // something went wrong
                }
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

    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();

                    for(Message message: messages)
                        Log.d(TAG, "Message stuff: " + message.getPostId());

                    mMessages.addAll(messages);
//                    holdKey.get(postId).addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    private String getDate(ParseObject parseObject) {
        Date date = parseObject.getCreatedAt();
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        String time = TimeFormatter.getTimeDifference(df.format(date));
        return time;
    }

    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
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

    private void goToActivity(Class targetClass) {

        Intent i = new Intent(this, targetClass);
        startActivity(i);
        finish();
    }

}