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
import com.example.instagram2.Models.Message;
import com.example.instagram2.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    public static final String TAG = "ChatAdapter";

    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;


    ImageView ivPostPic;
    ImageView imageView2;

    //sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue);

   //     Glide.with(context).load(post.getImage().getUrl()).override(ViewGroup.LayoutParams.MATCH_PARENT, 200).centerCrop().into(ivImage);

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;

    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);


    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);

        }

        @Override
        public void bindMessage(Message message) {
            Glide.with(mContext)
                    .load(getProfileUrl(message.getUserId()))
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageOther);

            body.setText(message.getBody());
            name.setText(message.getUsername());
         //   tvUsername.setText(post.getUser().getUsername());
         //   name.setText(message.getU); // in addition to message show user ID
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView body;
        TextView tvNameOut;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            tvNameOut = (TextView) itemView.findViewById(R.id.tvNameOut);
        }

        @Override
        public void bindMessage(Message message) {

            Glide.with(mContext)
                    .load(getProfileUrl(message.getUserId()))
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageMe);
            body.setText(message.getBody());

            Log.d(TAG, "username: " + message.getUsername());
            tvNameOut.setText(message.getUsername());
           // gettingPostInfo(postId);
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
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.messages_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);

    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }

    public void gettingImagePostInfo(String objectId)
    {

        /*
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Post description: ");
                Intent i = new Intent(mContext, MainActivity.class);
                mContext.startActivity(i);
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        query.getInBackground(postId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    tvPost.setText(object.getString("description"));

                } else {
                    // something went wrong
                }
            }
        });
            */
    }

}


