package com.client.utedating.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;

public class MatchedActivity extends AppCompatActivity {
    ImageView imageViewCloseMatched,imageViewMatchedUserA, imageViewMatchedUserB;
    Button buttonCloseMatched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);

        initView();
        setData();
        handleEvent();
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