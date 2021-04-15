package com.example.instagram2.BookClasses;

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
import com.example.instagram2.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Context context;
    List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("BookAdapter", "onCreateViewHolder");
        View bookView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("BookAdapter", "onBindViewHolder" + position);
        Book book = books.get(position);
        holder.bind(book);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvUsername;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Book book) {
            tvUsername.setText(book.getTitle());
            tvTitle.setText(book.getTitle());
            Glide.with(context).load(book.getCoverPath()).into(ivImage);
        }
    }
}

