package com.client.utedating.adapters;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.events.IConversationListener;
import com.client.utedating.models.Conversation;
import com.client.utedating.models.User;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {
    private List<Conversation> conversationList;
    private String senderId;
    private IConversationListener listener;
    public ConversationAdapter(List<Conversation> conversationList, String senderId, IConversationListener listener) {
        this.conversationList = conversationList;
        this.senderId = senderId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        User receiver = null;
        for(int i  = 0; i < conversation.getParticipants().size(); i++){
            if(!conversation.getParticipants().get(i).get_id().equals(senderId)){
                receiver = conversation.getParticipants().get(i);
            }
        }
        holder.text_name.setText(receiver.getName());
        holder.text_content.setText(conversation.getLastMessage().getContent());
        Glide
                .with(holder.itemView.getContext())
                .load(receiver.getAvatar())
                .into(holder.thumbnail);
        if(conversation.getLastMessage().getReceiver().equals(senderId)){
            holder.layout_dot_indicator.setVisibility(View.VISIBLE);
            holder.text_content.setTypeface(null, Typeface.BOLD);
        }
        User finalReceiver = receiver;
        holder.layout_message_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick");
                listener.onConversationListener(finalReceiver, conversation.get_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(conversationList != null)
            return conversationList.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        RelativeLayout layout_dot_indicator, layout_message_content;
        TextView text_name, text_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            layout_dot_indicator = itemView.findViewById(R.id.layout_dot_indicator);
            layout_message_content = itemView.findViewById(R.id.layout_message_content);
            text_name = itemView.findViewById(R.id.text_name);
            text_content = itemView.findViewById(R.id.text_content);
        }
    }
}
