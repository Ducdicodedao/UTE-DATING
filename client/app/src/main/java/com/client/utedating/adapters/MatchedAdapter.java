package com.client.utedating.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.events.IUserListener;
import com.client.utedating.models.Like;
import com.client.utedating.models.User;

import java.util.List;

public class MatchedAdapter extends RecyclerView.Adapter<MatchedAdapter.MyViewHolder> {
    private List<User> userList;
    private IUserListener listener;
    public MatchedAdapter(List<User> userList, IUserListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_like_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.likeName.setText(user.getName());

        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .into(holder.likeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserListener(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(userList != null)
            return userList.size();
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout likeLayout;
        TextView likeName;
        ImageView likeImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            likeLayout = itemView.findViewById(R.id.layout_like);
            likeName = itemView.findViewById(R.id.text_like_name);
            likeImage = itemView.findViewById(R.id.circle_image_like);
        }
    }

}
