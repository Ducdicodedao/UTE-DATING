package com.client.utedating.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.client.utedating.R;
import com.client.utedating.models.MessageItem;
import com.client.utedating.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.MyViewHolder> {
    private List<User> userList;

    public LikedAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        Glide
                .with(holder.itemView.getContext())
                .load(user.getAvatar())
                //Độ mờ 25, thu nhỏ ảnh đi 0 lần
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(100, 1)))
                .placeholder(R.drawable.img_holder)
                .into(holder.imageViewLiked);

        holder.textViewLikedName.getBackground().setAlpha(128);
        holder.textViewLikedFaculty.getBackground().setAlpha(128);
    }

    @Override
    public int getItemCount() {
        if (userList != null)
            return userList.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewLiked;
        TextView textViewLikedName;
        TextView textViewLikedFaculty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLiked = itemView.findViewById(R.id.imageViewLiked);
            textViewLikedName = itemView.findViewById(R.id.textViewLikedName);
            textViewLikedFaculty = itemView.findViewById(R.id.textViewLikedFaculty);

        }
    }
}
