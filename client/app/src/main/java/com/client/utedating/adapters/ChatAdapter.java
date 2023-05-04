package com.client.utedating.adapters;

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
import com.client.utedating.models.Message;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Message> messageList;
    private String avatar;
    private String receiverId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<Message> messageList, String avatar, String receiverId) {
        this.messageList = messageList;
        this.avatar = avatar;
        this.receiverId = receiverId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == VIEW_TYPE_SENT)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_right_message, parent, false);
            return new SentMessageViewHolder(v);
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_left_message, parent, false);
            return new ReceivedMessageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        PrettyTime prettyTime = new PrettyTime(new Locale("vi"));
        String formattedTime = prettyTime.format(message.getSentAt());

        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).textViewSentMessage.setText(message.getContent());
            ((SentMessageViewHolder) holder).textViewSentTime.setText(formattedTime);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((SentMessageViewHolder) holder).textViewSentTime.getVisibility() == View.GONE)
                    {
                        ((SentMessageViewHolder) holder).textViewSentTime.setVisibility(View.VISIBLE);
                    }
                    else{
                        ((SentMessageViewHolder) holder).textViewSentTime.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            ((ReceivedMessageViewHolder) holder).textViewReceivedMessage.setText(message.getContent());
            ((ReceivedMessageViewHolder) holder).textViewReceivedTime.setText(formattedTime);
            Glide
                    .with(holder.itemView.getContext())
                    .load(avatar)
                    .into(((ReceivedMessageViewHolder) holder).imageViewAvatarReceived);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((ReceivedMessageViewHolder) holder).textViewReceivedTime.getVisibility() == View.GONE)
                    {
                        ((ReceivedMessageViewHolder) holder).textViewReceivedTime.setVisibility(View.VISIBLE);
                    }
                    else{
                        ((ReceivedMessageViewHolder) holder).textViewReceivedTime.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getReceiver().equals(receiverId)){
            return VIEW_TYPE_RECEIVED;
        }
        else{
            return VIEW_TYPE_SENT;
        }
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder{
        TextView textViewSentMessage;
        TextView textViewSentTime;
        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.textViewSentMessage);
            textViewSentTime = itemView.findViewById(R.id.textViewSentTime);
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewAvatarReceived;
        TextView textViewReceivedMessage, textViewReceivedTime;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatarReceived = itemView.findViewById(R.id.imageViewAvatarReceived);
            textViewReceivedMessage = itemView.findViewById(R.id.textViewReceivedMessage);
            textViewReceivedTime = itemView.findViewById(R.id.textViewReceivedTime);
        }
    }
}
