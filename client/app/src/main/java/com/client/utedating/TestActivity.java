package com.client.utedating;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.client.utedating.adapters.ChatAdapter;
import com.client.utedating.models.Message;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TestActivity extends AppCompatActivity {
    private EditText edtSender, edtReceiver, edtMessage;
    private Button btnJoin, btnSend;
    private RecyclerView rcvChat;
    private ArrayList<Message> mMessageList;
    private ChatAdapter chatAdapter;
    public static String SenderID = "";

    Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            mSocket = IO.socket("http://10.0.2.2:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        edtSender = findViewById(R.id.edtSender);
        edtReceiver = findViewById(R.id.edtReceiver);
        edtMessage = findViewById(R.id.edtMessage);
        btnJoin = findViewById(R.id.btnJoin);
        btnSend = findViewById(R.id.btnSend);
        rcvChat = findViewById(R.id.rcvChat);

        mMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(mMessageList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvChat.setLayoutManager(linearLayoutManager);
        rcvChat.setAdapter(chatAdapter);

        mSocket.connect();
        mSocket.on("getMessage", onGetMessage);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("addUser", edtSender.getText().toString());
                SenderID = edtSender.getText().toString();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Message message = new Message("1", edtMessage.getText().toString(), edtReceiver.getText().toString());
                mSocket.emit("sendMessage",  gson.toJson(message));
                onLoadMessage("1", edtMessage.getText().toString(), edtReceiver.getText().toString());
            }
        });
    }

    private Emitter.Listener onGetMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    onLoadMessage(data.optString("conversationId"), data.optString("message"), data.optString("receiverId"));
                }
            });
        }
    };

    private void onLoadMessage(String conversationId, String message, String receiverId) {
        Message m = new Message(conversationId, message, receiverId);

        mMessageList.add(m);
        chatAdapter.notifyItemInserted(mMessageList.indexOf(m));
        rcvChat.smoothScrollToPosition(mMessageList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("disconnect");
        mSocket.disconnect();
    }
}