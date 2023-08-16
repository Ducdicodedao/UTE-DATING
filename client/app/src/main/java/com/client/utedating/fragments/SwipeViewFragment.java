package com.client.utedating.fragments;


import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.client.utedating.R;
import com.client.utedating.Utils;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.models.Profile;
import com.client.utedating.models.SubTinderCard;
import com.client.utedating.models.TinderCard;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.models.UsersModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeViewFragment extends Fragment {


    SharedPreferencesClient sharedPreferencesClient;
    User user;
    List<User> userList = new ArrayList<>();
    List<Profile> profileList = new ArrayList<>();
    UserApiService userApiService;
    MainActivity mainActivity;
    private View rootLayout;
    private FloatingActionButton fabBack, fabLike, fabSkip, fabSuperLike, fabBoost;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    public SwipeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe_view, container, false);

        return rootLayout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        handleEvent();

//        int topMargin = Utils.dpToPx(100);
        int bottomMargin = Utils.dpToPx(58);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin )
                        .setViewGravity(Gravity.TOP)
//                      .setViewHeight(windowSize.y - bottomMargin - topMargin)
//                        .setViewGravity(Gravity.BOTTOM)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        //Ngăn không cho người dùng lướt nữa
        //mSwipeView.lockViews();

        fetchData();

//        mSwipeView.getBuilder().getSwipePlaceHolderView().setFlingListener(new OnCardSwipedListener() {
//            @Override
//            public void onCardSwiped(Direction direction) {
//                currentCardPosition = swipePlaceHolderView.getCurrentPosition();
//                if (currentCardPosition == profileList.size() - 1) {
//                    swipePlaceHolderView.lockViews(); // Đóng khóa swipe trên tất cả các view trong SwipePlaceHolderView
//                }
//            }
//        });

//        for (Profile profile : Utils.loadProfiles(getActivity())) {
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }


    }

    private void fetchData() {
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        sharedPreferencesClient = new SharedPreferencesClient(mainActivity);
        user = sharedPreferencesClient.getUserInfo("user");

        sharedPreferencesClient.setCardCount("cardcount", 0);
        userApiService.getUsersByDatewith(user.get_id()).enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                if (response.isSuccessful()) {
                    userList = response.body().getResult();
                    for (User u : userList) {
                        profileList.add(new Profile(u.get_id(), u.getName(), u.getAvatar(), getAge(u.getBirthday()), u.getFaculty(), String.valueOf(calculateDistance((List<Double>) u.getLocation().get("coordinates"),(List<Double>) user.getLocation().get("coordinates"))), u.getAbout(), u.getInterests(), u.getGender(), u.getToken()));
                    }
                    for (Profile profile : profileList) {
                        Map<String, String> body = new HashMap<>();
                        body.put("userId", user.get_id());
                        body.put("swipedUserId", profile.get_id());
                        mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, body, profileList.size()));
                    }
                    mSwipeView.addView(new SubTinderCard());
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {

            }
        });

    }

    private void handleEvent() {
//        fabSkip.setOnClickListener(v -> {
//            animateFab(fabSkip);
//            mSwipeView.doSwipe(false);
//        });
//
//        fabLike.setOnClickListener(v -> {
//            animateFab(fabLike);
//            mSwipeView.doSwipe(true);
//        });
//
//        fabBoost.setOnClickListener(v -> animateFab(fabBoost));
//        fabSuperLike.setOnClickListener(v -> animateFab(fabSuperLike));
//        fabBack.setOnClickListener(v -> animateFab(fabBack));
    }

    private void initView(View view) {
        mSwipeView = view.findViewById(R.id.swipeView);
//        fabBack = view.findViewById(R.id.fabBack);
//        fabLike = view.findViewById(R.id.fabLike);
//        fabSkip = view.findViewById(R.id.fabSkip);
//        fabSuperLike = view.findViewById(R.id.fabSuperLike);
//        fabBoost = view.findViewById(R.id.fabBoost);
        mContext = getActivity();
        mainActivity = (MainActivity) getActivity();
    }


    private void animateFab(final FloatingActionButton fab) {
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

    private int getAge(String birthday) {
        // Chuyển đổi chuỗi ngày sinh thành đối tượng LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth = LocalDate.parse(birthday, formatter);

        // Tính tuổi từ ngày sinh đến ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dateOfBirth, currentDate).getYears();
        return age;
    }

    public static double calculateDistance(List<Double> location1, List<Double> location2) {
        final double RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(location2.get(1) - location1.get(1));
        double lonDistance = Math.toRadians(location2.get(0) - location1.get(0));
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(location1.get(1))) * Math.cos(Math.toRadians(location2.get(1) ))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS_OF_EARTH_KM * c;
        return Math.round(distance * 100.0) / 100.0;
    }
}
