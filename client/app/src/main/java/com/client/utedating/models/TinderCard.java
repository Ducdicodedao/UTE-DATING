package com.client.utedating.models;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.activities.MatchedActivity;
import com.client.utedating.retrofit.NotificationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.RetrofitNotification;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Layout(R.layout.adapter_tinder_card)
public class TinderCard {
    @View(R.id.profileImageView)
    public ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    public TextView nameAgeTxt;

    @View(R.id.facultyTxt)
    public TextView locationNameTxt;

    public Profile mProfile;
    public Context mContext;
    public SwipePlaceHolderView mSwipeView;
    public Map<String, String> mBody;
    public int length;

    public UserApiService userApiService;
    int cardCount;

    NotificationApiService notificationApiService;
    public TinderCard() {
    }

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView, Map<String, String> body, int length) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        mBody = body;
        this.length = length;
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

    }

    @Resolve
    public void onResolved() {
        Glide.with(mContext).load(mProfile.getImageUrl()).placeholder(R.drawable.img_holder).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getFaculty());
    }

    @SwipeOut
    public void onSwipedOut() {
        Log.e("TAG", "onSwipedOut");
        //mSwipeView.addView(this);

        cardCount = MySharedPreferences.getIntSharedPreference(mContext, "cardcount");
        Log.e("TAG", String.valueOf(cardCount));
        if (cardCount == length - 1) {
            mSwipeView.lockViews();
        } else {
            cardCount++;
            MySharedPreferences.setIntSharedPreference(mContext,"cardcount", cardCount);
        }
        userApiService.addUserSwipedLeft(mBody).enqueue(new Callback<NoResultModel>() {
            @Override
            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                if(response.isSuccessful()){
                    Log.e("TAG", response.message());
                }
            }

            @Override
            public void onFailure(Call<NoResultModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn() {
        Log.e("TAG", "onSwipedIn");
        cardCount = MySharedPreferences.getIntSharedPreference(mContext,"cardcount");
        Log.e("TAG", String.valueOf(cardCount));
        if (cardCount == length - 1) {
            mSwipeView.lockViews();
        } else {
            cardCount++;
            MySharedPreferences.setIntSharedPreference(mContext,"cardcount", cardCount);
        }

        userApiService.isUserSwipedRight(mBody.get("userId"), mBody.get("swipedUserId")).enqueue(new Callback<NoResultModel>() {
            @Override
            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("isSwipedRight")){
                        userApiService.addUserMatched(mBody).enqueue(new Callback<NoResultModel>() {
                            @Override
                            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                                if(response.isSuccessful()){
                                    Log.e("TAG", response.body().getMessage());
                                    User user = MySharedPreferences.getUserInfo(mContext,"user");
                                    Intent i = new Intent(mContext, MatchedActivity.class);
                                    i.putExtra("userId", user.get_id());
                                    i.putExtra("swipedUserId", mProfile.get_id());
                                    i.putExtra("userAvatar",user.getAvatar() );
                                    i.putExtra("swipedUserAvatar", mProfile.getImageUrl());
                                    i.putExtra("swipedUserToken", mProfile.getToken());
                                    mContext.startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<NoResultModel> call, Throwable t) {
                                Log.e("TAG", t.getMessage());
                            }
                        });
                    }
                    else{
                        userApiService.addUserSwipedRight(mBody).enqueue(new Callback<NoResultModel>() {
                            @Override
                            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                                if (response.isSuccessful()) {
                                    Log.e("TAG", response.body().getMessage());

                                    //Send notification to swiped User
                                    sendNotification();
                                }
                            }

                            @Override
                            public void onFailure(Call<NoResultModel> call, Throwable t) {
                                Log.e("TAG", t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<NoResultModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });

    }

    @SwipeInState
    public void onSwipeInState() {
        Log.e("TAG", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.e("TAG", "onSwipeOutState");
    }

    @Click(R.id.profileImageView)
    public void onClick() {
        Log.e("TAG", "onClick");

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_user_info);

        setDataDialog(dialog);
        // Thiết lập các thuộc tính cho dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Đặt margin cho dialog
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(params);

        // Hiển thị dialog
        dialog.show();
    }

    public void setDataDialog(Dialog dialog){
        TextView nameAgeTxt = dialog.findViewById(R.id.textViewNameAndAge);
        TextView textViewGender = dialog.findViewById(R.id.textViewGender);
        TextView textViewFaculty = dialog.findViewById(R.id.textViewFaculty);
        TextView textViewLocation = dialog.findViewById(R.id.textViewLocation);
        TextView textViewAbout = dialog.findViewById(R.id.textViewAbout);
        ChipGroup chipGroupInterest = dialog.findViewById(R.id.chipGroupInterest);

        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());

        textViewGender.setText(mProfile.getGender().equals("male") ? "♂️ Nam" : "♀️ Nữ" );
        textViewFaculty.setText("🎓 Khoa "+ mProfile.getFaculty());
        textViewLocation.setText("📌 Cách xa "+mProfile.getLocation()+"km");
        textViewAbout.setText(mProfile.getAbout());

        for (int i = 0; i < chipGroupInterest.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupInterest.getChildAt(i);
            String chipText = chip.getText().toString();
            int flag = 0;
            Log.d("ChipText", chipText);
            for(int j = 0; j < mProfile.getInterests().size(); j++){
                if(chipText.equals(mProfile.getInterests().get(j))){
                    flag = 1;
                }
            }
            if(flag == 0){
                chip.setVisibility(android.view.View.GONE);
            }
        }
    }
    private void sendNotification() {
        Map<String, String> notification = new HashMap<>();
        notification.put("title", "UTE DATING");
        notification.put("body", "Ai đó vừa mới thích bạn 💙");
        NotificationSend notificationSend = new NotificationSend(mProfile.getToken(), notification);
        notificationApiService = RetrofitNotification.getInstance().create(NotificationApiService.class);
        notificationApiService.sendNotification(notificationSend).enqueue(new Callback<NotificationReceived>() {
            @Override
            public void onResponse(Call<NotificationReceived> call, Response<NotificationReceived> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", "Notification Send Successfully");
                }
            }

            @Override
            public void onFailure(Call<NotificationReceived> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });

    }
}