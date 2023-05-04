package com.client.utedating;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.client.utedating.adapters.ChatTestAdapter;
import com.client.utedating.models.MessageSocket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TestActivity extends AppCompatActivity {
//    private EditText edtSender, edtReceiver, edtMessage;
//    private Button btnJoin, btnSend;
//    private RecyclerView rcvChat;
//    private ArrayList<MessageSocket> mMessageListSocket;
//    private ChatTestAdapter chatTestAdapter;
//    public static String SenderID = "";
//
//    Socket mSocket;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        try {
//            mSocket = IO.socket("http://10.0.2.2:5000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        edtSender = findViewById(R.id.edtSender);
//        edtReceiver = findViewById(R.id.edtReceiver);
//        edtMessage = findViewById(R.id.edtMessage);
//        btnJoin = findViewById(R.id.btnJoin);
//        btnSend = findViewById(R.id.btnSend);
//        rcvChat = findViewById(R.id.rcvChat);
//
//        mMessageListSocket = new ArrayList<>();
//        chatTestAdapter = new ChatTestAdapter(mMessageListSocket);
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvChat.setLayoutManager(linearLayoutManager);
//        rcvChat.setAdapter(chatTestAdapter);
//
//        mSocket.connect();
//        mSocket.on("getMessage", onGetMessage);
//
//        btnJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSocket.emit("addUser", edtSender.getText().toString());
//                SenderID = edtSender.getText().toString();
//            }
//        });
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MessageSocket messageSocket = new MessageSocket("1", edtMessage.getText().toString(), edtReceiver.getText().toString());
//                mSocket.emit("sendMessage",  gson.toJson(messageSocket));
//                onLoadMessage("1", edtMessage.getText().toString(), edtReceiver.getText().toString());
//            }
//        });
//    }
//
//    private Emitter.Listener onGetMessage = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    onLoadMessage(data.optString("conversationId"), data.optString("message"), data.optString("receiverId"));
//                }
//            });
//        }
//    };
//
//    private void onLoadMessage(String conversationId, String message, String receiverId) {
//        MessageSocket m = new MessageSocket(conversationId, message, receiverId);
//
//        mMessageListSocket.add(m);
//        chatTestAdapter.notifyItemInserted(mMessageListSocket.indexOf(m));
//        rcvChat.smoothScrollToPosition(mMessageListSocket.size() - 1);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mSocket.emit("disconnect");
//        mSocket.disconnect();
//    }
}