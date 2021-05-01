package com.example.instagram2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram2.Activities.BookActivity;
import com.example.instagram2.Activities.ChatActivity;
import com.example.instagram2.Activities.Heart;
import com.example.instagram2.Fragments.PostFragment;
import com.example.instagram2.Models.Post;
import com.example.instagram2.R;
import com.example.instagram2.TimeFormatter;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.instagram2.Profile.getProfileUrl;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    public static final String TAG = "PostsAdapter";

    private GestureDetector mGestureDetector;
    private Heart mHeart;

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
        private ImageView ivProfilePicture;
        private TextView tvDescription;
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvReply;
        private Button btnDelete;
        private ImageView mHeartWhite;
        private ImageView mHeartRed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvPostTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReply = itemView.findViewById(R.id.tvReply);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            mHeartWhite = itemView.findViewById(R.id.image_heart_post);
            mHeartRed = itemView.findViewById(R.id.image_heart_red_post_invi);

           /* mHeartRed.setVisibility(View.GONE);
            mHeartWhite.setVisibility(View.VISIBLE);
            mHeart = new Heart(mHeartWhite, mHeartRed);

            mGestureDetector = new GestureDetector(context, new GestureListener());*/

//            testToggle();
        }

        private void testToggle ()
        {
            mHeartRed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: red heart detected");
                    return mGestureDetector.onTouchEvent(event);
                }
            });

            mHeartWhite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: white heart detected");
                    return mGestureDetector.onTouchEvent(event);
                }
            });
        }

        public class GestureListener extends GestureDetector.SimpleOnGestureListener
        {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap: double tap detected");
                mHeart.toggleLike();
                return true;
            }
        }

        public void helpDelete(String objectId) {
            ParseQuery<ParseObject> posts = ParseQuery.getQuery("Post");
            // Query parameters based on the item name
            posts.whereEqualTo("objectId", objectId);

            posts.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> player, ParseException e) {
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
                }

                ;
            });
        }

        public void bind(Post post) {

            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            tvTime.setText(getDate(post));
            String profileURL = getProfileUrl(post.getUser().getObjectId());
            Glide.with(context)
                    .load(profileURL)
                    .circleCrop() // create an effect of a round profile picture
                    .into(ivProfilePicture);

            String title = post.getKeyBookTitle();
            if(title != null) {
                tvTitle.setText(title);
            } else {tvTitle.setVisibility(View.GONE);}

            mHeartRed.setVisibility(View.GONE);
            mHeartWhite.setVisibility(View.VISIBLE);
            mHeart = new Heart(mHeartWhite, mHeartRed);

            mGestureDetector = new GestureDetector(context, new GestureListener());

            testToggle();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Object ID : " + post.getObjectId());

                    helpDelete(post.getObjectId());

                    notifyDataSetChanged();
                }
            });

            

            tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ChatActivity.class);
                    i.putExtra("user", post.getObjectId());
                    i.putExtra("username", post.getUser().getUsername());

                    if (post.getImage() != null) {
                        i.putExtra("imageP", post.getImage().getUrl());
                    }
                    context.startActivity(i);
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


            if (image != null) {
                ivImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(post.getImage().getUrl()).transform(new RoundedCorners(80)).centerCrop().into(ivImage);
            } else {
                ivImage.setImageBitmap(null);
                ivImage.setVisibility(View.GONE);
            }
        }


        private String getDate(Post post) {
            Date date = post.getCreatedAt();
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            String time = TimeFormatter.getTimeDifference(df.format(date));
            return time;
        }
    }
}


