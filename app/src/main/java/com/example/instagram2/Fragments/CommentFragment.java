package com.example.instagram2.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instagram2.Models.Comment;
import com.example.instagram2.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static android.app.Activity.RESULT_OK;

public class CommentFragment extends Fragment {

    public static final String TAG = "CommentFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private EditText etComment;
    private ImageView ivPostComment;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etComment = view.findViewById(R.id.etComment);
        ivPostComment = view.findViewById(R.id.ivPostComment);

        etComment.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"Count: " + etComment.length());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //queryPosts();

        ivPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etComment.getText().toString();

                if(description.isEmpty())
                {
                    Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComment(description, currentUser);
            }
        });

    }

    private void saveComment(String description, ParseUser currentUser) {
        Comment comment = new Comment();

        comment.setKeyComment(description);
        comment.setUser(currentUser);

        comment.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Error while saving", e);
                    //      Toast.makeText(MainActivity.this, "error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful");
                etComment.setText("");
            }
        });

    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                // Load the taken image into a preview
                ivPostComment.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    } */

}