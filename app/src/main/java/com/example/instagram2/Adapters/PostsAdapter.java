package com.example.instagram2.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.parse.ParseFile;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    public static final String TAG = "PostsAdapter";

    public PostsAdapter(Context context, List<Post> posts)
    {
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

            tvTitle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookActivity.class);
                    i.putExtra("Book_ID", "x9x_DwAAQBAJ");
                    context.startActivity(i);
                }
            });

            tvReply.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ChatActivity.class);
                    context.startActivity(i);
                    //tvPost.setText((CharSequence) tvDescription);
                }
            });
        }

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);

                /*
                ParseQuery<ParseObject> posts = ParseQuery.getQuery("Post");
                // Query parameters based on the item name
                posts.whereEqualTo("objectId", "QHjRWwgEtd");
                posts.findInBackground(new FindCallback<ParseObject>() {
                  @Override
                  public void done(final List<ParseObject> post, ParseException e) {
                    if (e == null) {
                      post.get(0).deleteInBackground(new DeleteCallback() {
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
                }
                 */
            }
        });
    }

        public void bind(Post post) {

            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            tvTime.setText(getDate(post));
            //tvTime.setText((CharSequence) post.getCreatedAt());
            if(image != null)
                Glide.with(context).load(post.getImage().getUrl())
                        .override(ViewGroup.LayoutParams.MATCH_PARENT, 200).centerCrop().into(ivImage);
        }

        private String getDate(Post post){
            Date date = post.getCreatedAt();
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            String time = TimeFormatter.getTimeDifference(df.format(date));
            return time;
        }
    }
}
