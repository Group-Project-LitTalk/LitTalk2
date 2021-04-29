package com.example.instagram2.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram2.Models.Comment;
import com.example.instagram2.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final int COMMENT_OUTGOING = 123;

    private List<Comment> mComments;
    private Context mContext;
    private String mUserId;

    public CommentAdapter(Context context, String userId, List<Comment> comments) {
        mComments = comments;
        this.mUserId = userId;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("CommentAdapter", "onCreateViewHolder");

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View commentView = inflater.inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(commentView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
           ImageView imageMe;
           TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);

        }

             public void bind(Comment comment) {
             Glide.with(mContext)
                     .load(getProfileUrl(comment.getUserId()))
                     .circleCrop() // create an effect of a round profile picture
                     .into(imageMe);
             body.setText(comment.getBody());
         }

    }

    private static String getProfileUrl(final String userId) {
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

    @Override
    public int getItemCount() {
        return mComments.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("CommentAdapter", "onBindViewHolder" + position);

        Comment comment = mComments.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return COMMENT_OUTGOING;
        }
        return 0;
    }

    private boolean isMe(int position) {
        Comment comment = mComments.get(position);
        return comment.getUserId() != null && comment.getUserId().equals(mUserId);
    }
}


