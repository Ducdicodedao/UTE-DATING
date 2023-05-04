package com.client.utedating.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.adapters.ChatAdapter;
import com.client.utedating.models.Message;
import com.client.utedating.models.MessageModel;
import com.client.utedating.models.MessageSocket;
import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ImageView imageViewAvatar;
    TextView textViewName;
    RecyclerView recyclerViewChat;
    EditText editTextChat;
    AppCompatImageButton buttonSentMessage;
    AppCompatImageView imageViewChatBack;
    SharedPreferencesClient sharedPreferencesClient;
    User user;
    ConversationApiService conversationApiService;

    String receiverId;
    String receiverName;
    String receiverAvatar;
    String conversationId;

    List<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;

    Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        setData();
        setRecyclerView();
        setSocket();
        fetchData();
        handleEvent();
    }

    private void initView() {
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextChat = findViewById(R.id.editTextChat);
        buttonSentMessage = findViewById(R.id.buttonSentMessage);
        imageViewChatBack = findViewById(R.id.imageViewChatBack);
        buttonSentMessage.setEnabled(false);

        sharedPreferencesClient = new SharedPreferencesClient(this);
        user = sharedPreferencesClient.getUserInfo("user");
        conversationApiService = RetrofitClient.getInstance().create(ConversationApiService.class);
    }

    private void setData() {
        receiverId = getIntent().getStringExtra("receiverId");
        receiverName= getIntent().getStringExtra("name");
        receiverAvatar = getIntent().getStringExtra("avatar");
        conversationId = getIntent().getStringExtra("conversationId");

        Log.e("TAG", conversationId);

        textViewName.setText(receiverName);
        Glide
                .with(this)
                .load(receiverAvatar)
                .centerCrop()
                .into(imageViewAvatar);
    }
    private void setRecyclerView(){
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewChat.setHasFixedSize(true);

        chatAdapter = new ChatAdapter(messageList, receiverAvatar, user.get_id());
        recyclerViewChat.setAdapter(chatAdapter);
    }
    private void setSocket() {
        try {
            mSocket = IO.socket("http://10.0.2.2:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        mSocket.on("getMessage", onGetMessage);
        mSocket.emit("addUser", user.get_id());
    }

    private void fetchData() {
        conversationApiService.getMessages(conversationId).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if(response.isSuccessful()){
                    Log.e("TAG", response.body().getMessage());
                    messageList.addAll(response.body().getResult());
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void handleEvent(){
        buttonSentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MessageSocket messageSocket = new MessageSocket(conversationId,editTextChat.getText().toString().trim(), receiverId);
                mSocket.emit("sendMessage",  gson.toJson(messageSocket));
                onLoadMessage(conversationId, editTextChat.getText().toString().trim(), receiverId);

                Map<String, String> body = new HashMap<>();

                body.put("receiver", receiverId);
                body.put("message", editTextChat.getText().toString().trim());
                conversationApiService.sendMessage(conversationId, body).enqueue(new Callback<NoResultModel>() {
                    @Override
                    public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                        if(response.isSuccessful()){
                            Log.e("TAG", response.body().getMessage());
                            editTextChat.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<NoResultModel> call, Throwable t) {
                        Log.e("TAG", t.getMessage());
                    }
                });

            }
        });

        editTextChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lấy số ký tự đang nhập
                int length = s.toString().length();
                if(length == 0){
                    buttonSentMessage.setEnabled(false);
                }
                else{
                    buttonSentMessage.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageViewChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private Emitter.Listener onGetMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "onGetMessage");
                    JSONObject data = (JSONObject) args[0];
                    onLoadMessage(data.optString("conversationId"), data.optString("message"), data.optString("receiverId"));
                }
            });
        }
    };

    private void onLoadMessage(String conversationId, String message, String receiverId) {
//        MessageSocket m = new MessageSocket(conversationId, message, receiverId);
        Message m = new Message();
        m.setReceiver(receiverId);
        m.setContent(message);
        m.setSentAt(new Date());

        addMessageToList(m);
    }

    private void addMessageToList(Message m){
        messageList.add(m);
        chatAdapter.notifyItemInserted(messageList.indexOf(m));
        recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("disconnection");
        mSocket.disconnect();
    }
}