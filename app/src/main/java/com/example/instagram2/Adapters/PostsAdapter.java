package com.example.instagram2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram2.BookClasses.BookActivity;
import com.example.instagram2.ChatActivity;
import com.example.instagram2.MainActivity;
import com.example.instagram2.Post;
import com.example.instagram2.R;
import com.example.instagram2.TimeFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    public static final String TAG = "PostsAdapter";
    protected PostsAdapter adapter;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvReply;
        private Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvPostTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReply = itemView.findViewById(R.id.tvReply);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ChatActivity.class);
                    context.startActivity(i);
                    //tvPost.setText((CharSequence) tvDescription);
                }
            });

        }

        public void helpDelete(String objectId)
        {
            ParseQuery <ParseObject> posts = ParseQuery.getQuery("Post");
            // Query parameters based on the item name
            posts.whereEqualTo("objectId", objectId);

            posts.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List <ParseObject> player, ParseException e) {
                    if (e == null) {
                        player.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success
                                } else {
                                    // Failed
                                }
                            }
                        });
                    } else {
                        // Something is wrong
                    }
                };
            });

        }

        public void replyPost(String objectId)
        {
            ParseQuery<ParseObject> post = ParseQuery.getQuery("Post");
            // Retrieve the object by id
            post.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject player, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        player.put("yearOfBirth", 1998);
                        player.put("emailContact", "a.wed@domain.io");
                        player.saveInBackground();
                    } else {
                        // Failed
                    }
                }
            });
        }

        public void bind(Post post) {

            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            tvTime.setText(getDate(post));
            tvTitle.setText(post.getKeyBookTitle());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Object ID : " + post.getObjectId());

                    helpDelete(post.getObjectId());

                    notifyDataSetChanged();
                }
            });

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookActivity.class);
                    i.putExtra("Book_ID", post.getKeyBookId());
                    context.startActivity(i);
                }
            });

            if (image != null)
                Glide.with(context).load(post.getImage().getUrl())
                        .override(ViewGroup.LayoutParams.MATCH_PARENT, 200).centerCrop().into(ivImage);

        }

        private String getDate(Post post) {
            Date date = post.getCreatedAt();
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            String time = TimeFormatter.getTimeDifference(df.format(date));
            return time;
        }
    }
}


