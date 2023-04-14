package com.client.utedating.adapters;

import static com.client.utedating.TestActivity.SenderID;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.client.utedating.R;
import com.client.utedating.models.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<Message> mReceiveUserList;

    public ChatAdapter(List<Message> mReceiveUserList) {
        this.mReceiveUserList = mReceiveUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (mReceiveUserList.get(i).getReceiverId().equals(SenderID)) {
            viewHolder.imgSender.setVisibility(View.GONE);
            viewHolder.imgReciver.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSender.setVisibility(View.VISIBLE);
            viewHolder.imgReciver.setVisibility(View.GONE);
        }
        viewHolder.txtMessUser.setText(mReceiveUserList.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
        return mReceiveUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgReciver;
        private final ImageView imgSender;
        private final TextView txtMessUser;

        ViewHolder(View itemView) {
            super(itemView);
            imgReciver = itemView.findViewById(R.id.imgReciver);
            imgSender = itemView.findViewById(R.id.imgSender);
            txtMessUser = itemView.findViewById(R.id.txtMessUser);
        }
    }
}