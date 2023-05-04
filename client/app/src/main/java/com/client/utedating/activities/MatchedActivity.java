package com.client.utedating.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.models.Conversation;
import com.client.utedating.models.NoResultModel;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchedActivity extends AppCompatActivity {
    ImageView imageViewCloseMatched,imageViewMatchedUserA, imageViewMatchedUserB;
    Button buttonCloseMatched;

    ConversationApiService conversationApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);

        initView();
        setData();
        handleEvent();
        createConversation();
    }

    private void createConversation() {
        String userId = getIntent().getStringExtra("userId");
        String swipedUserId = getIntent().getStringExtra("swipedUserId");

        Map<String, String> body = new HashMap<>();
        body.put("senderId", userId);
        body.put("receiverId", swipedUserId);

        conversationApiService = RetrofitClient.getInstance().create(ConversationApiService.class);
        conversationApiService.createConversation(body).enqueue(new Callback<NoResultModel>() {
            @Override
            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                if(response.isSuccessful()){
                    Log.e("TAG", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<NoResultModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void handleEvent() {
        imageViewCloseMatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCloseMatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        String userAvatar = getIntent().getStringExtra("userAvatar");
        String swipedUserAvatar = getIntent().getStringExtra("swipedUserAvatar");

        Glide
                .with(this)
                .load(userAvatar)
                .centerCrop()
                .into(imageViewMatchedUserA);

        Glide
                .with(this)
                .load(swipedUserAvatar)
                .centerCrop()
                .into(imageViewMatchedUserB);


    }

    public void initView(){
        imageViewCloseMatched = findViewById(R.id.imageViewCloseMatched);
        imageViewMatchedUserA = findViewById(R.id.imageViewMatchedUserA);
        imageViewMatchedUserB = findViewById(R.id.imageViewMatchedUserB);
        buttonCloseMatched = findViewById(R.id.buttonCloseMatched);
    }
}