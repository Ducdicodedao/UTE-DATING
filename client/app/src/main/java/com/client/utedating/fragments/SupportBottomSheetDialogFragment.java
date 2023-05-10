package com.client.utedating.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.client.utedating.R;
import com.client.utedating.activities.ChatActivity;
import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SupportBottomSheetDialogFragment extends BottomSheetDialogFragment {
    LinearLayout linearLayoutSupport;
    LinearLayout linearLayoutReport;
    LinearLayout linearLayoutUnmatched;
    LinearLayout linearLayoutConfirmUnmatched;
    AppCompatButton buttonConfirmUnmatched;
    ChatActivity chatActivity;

    SharedPreferencesClient sharedPreferencesClient;
    User user;
    String receiverId;
    String conversationId;
    UserApiService userApiService;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setData(view);
        handleEvent();
    }

    private void handleEvent() {
        linearLayoutReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearLayoutUnmatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Ẩn linearLayoutSupport
                linearLayoutSupport.animate()
                        .alpha(0.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                linearLayoutSupport.setVisibility(View.GONE);
                            }
                        });

                // Hiện linearLayoutConfirmUnmatched
                linearLayoutConfirmUnmatched.setAlpha(0.0f);
                linearLayoutConfirmUnmatched.animate()
                        .alpha(1.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                linearLayoutConfirmUnmatched.setVisibility(View.VISIBLE);
                            }
                        });

            }
        });
        buttonConfirmUnmatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String , String > body = new HashMap<>();
                body.put("conversationId", conversationId);
                body.put("matchedUserId", receiverId);
                body.put("userId", user.get_id());
                userApiService.unMatched(body).enqueue(new Callback<NoResultModel>() {
                    @Override
                    public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                        if(response.isSuccessful()){
                            Log.e("TAG", response.body().getMessage());
                            chatActivity.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<NoResultModel> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void initView(View v) {
        linearLayoutSupport = v.findViewById(R.id.linearLayoutSupport);
        linearLayoutReport = v.findViewById(R.id.linearLayoutReport);
        linearLayoutUnmatched = v.findViewById(R.id.linearLayoutUnmatched);
        linearLayoutConfirmUnmatched = v.findViewById(R.id.linearLayoutConfirmUnmatched);
        buttonConfirmUnmatched = v.findViewById(R.id.buttonConfirmUnmatched);
        chatActivity = (ChatActivity) getActivity();
    }

    private void setData(View view) {
        sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        user = sharedPreferencesClient.getUserInfo("user");
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Lấy giá trị từ Bundle, ví dụ:
            receiverId = bundle.getString("receiverId");
            conversationId = bundle.getString("conversationId");
        }
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }
}