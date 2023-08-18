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
import com.client.utedating.retrofit.ReportApiService;
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

//    Report Form
    LinearLayout linearLayoutReportForm;
    LinearLayout linearLayoutReportDetail1, linearLayoutReportDetail2, linearLayoutReportDetail3, linearLayoutReportDetail4;
    LinearLayout linearLayoutConfirmReported;
    AppCompatButton buttonConfirmReported;


    ChatActivity chatActivity;

    SharedPreferencesClient sharedPreferencesClient;
    User user;
    String receiverId;
    String conversationId;

    UserApiService userApiService;
    ReportApiService reportApiService;

    String reportDetail = "";
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
                handleUnmatched();
            }
        });
        linearLayoutReport.setOnClickListener(new View.OnClickListener() {
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

                // Hiện linearLayoutReportForm
                linearLayoutReportForm.setAlpha(0.0f);
                linearLayoutReportForm.animate()
                        .alpha(1.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                linearLayoutReportForm.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        linearLayoutReportDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReportDetail("Tài khoản giả mạo");
            }
        });

        linearLayoutReportDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReportDetail("Chia sẻ nôi dung không phù hợp");
            }
        });

        linearLayoutReportDetail3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReportDetail("Hồ sơ của người chưa đủ 18 tuổi");
            }
        });

        linearLayoutReportDetail4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReportDetail("Ngôn từ vi pham tiêu chuẩn cộng đồng");
            }
        });

        buttonConfirmReported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendReport();
            }
        });
    }

    private void handleSendReport() {
        Map<String,String> body = new HashMap<>();
        body.put("sender", user.get_id());
        body.put("receiver", receiverId);
        body.put("title", reportDetail);
        Log.e("TAG", "SendReport");
        reportApiService.sendReport(body).enqueue(new Callback<NoResultModel>() {
            @Override
            public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                if (response.isSuccessful()){
                    Log.e("TAG", response.body().getMessage());
                    handleUnmatched();
                }
            }

            @Override
            public void onFailure(Call<NoResultModel> call, Throwable t) {

            }
        });
    }

    private void handleUnmatched() {
        Map<String,String> body = new HashMap<>();
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

    private void chooseReportDetail(String detail) {
        reportDetail = detail;
        // Ẩn linearLayoutReportForm
        linearLayoutReportForm.animate()
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linearLayoutReportForm.setVisibility(View.GONE);
                    }
                });

        // Hiện linearLayoutReportForm
        linearLayoutConfirmReported.setAlpha(0.0f);
        linearLayoutConfirmReported.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linearLayoutConfirmReported.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView(View v) {
        linearLayoutSupport = v.findViewById(R.id.linearLayoutSupport);
        linearLayoutReport = v.findViewById(R.id.linearLayoutReport);
        linearLayoutUnmatched = v.findViewById(R.id.linearLayoutUnmatched);
        linearLayoutConfirmUnmatched = v.findViewById(R.id.linearLayoutConfirmUnmatched);
        buttonConfirmUnmatched = v.findViewById(R.id.buttonConfirmUnmatched);

//      Report Form
        linearLayoutReportForm = v.findViewById(R.id.linearLayoutReportForm);
        linearLayoutReportDetail1 = v.findViewById(R.id.linearLayoutReportDetail1);
        linearLayoutReportDetail2 = v.findViewById(R.id.linearLayoutReportDetail2);
        linearLayoutReportDetail3 = v.findViewById(R.id.linearLayoutReportDetail3);
        linearLayoutReportDetail4 = v.findViewById(R.id.linearLayoutReportDetail4);
        linearLayoutConfirmReported = v.findViewById(R.id.linearLayoutConfirmReported);
        buttonConfirmReported = v.findViewById(R.id.buttonConfirmReported);

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
        reportApiService =RetrofitClient.getInstance().create(ReportApiService.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }
}