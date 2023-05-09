package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.adapters.ViewPagerAdapter;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenderUpdateFragment extends Fragment {
    Button buttonFemale, buttonMale;
    TextView buttonSubmitGender, buttonCancelGender;

    MainActivity mainActivity;
    String gender = "";

    UserApiService userApiService;
    public GenderUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gender_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonFemale = view.findViewById(R.id.buttonFemale);
        buttonMale = view.findViewById(R.id.buttonMale);
        buttonSubmitGender = view.findViewById(R.id.buttonSubmitGender);
        buttonCancelGender = view.findViewById(R.id.buttonCancelGender);

        mainActivity = (MainActivity) getActivity();

        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(mainActivity);
        User user = sharedPreferencesClient.getUserInfo("user");
        if (user.getGender().equals("female")) {
            buttonFemale.setBackgroundResource(R.drawable.button_shape10);
            int color = ContextCompat.getColor(mainActivity, R.color.white);
            buttonFemale.setTextColor(color);

            buttonMale.setBackgroundResource(R.drawable.button_shape10_normal);
            int colorMale = ContextCompat.getColor(mainActivity, R.color.black);
            buttonMale.setTextColor(colorMale);
        } else {
            buttonMale.setBackgroundResource(R.drawable.button_shape10);
            int color = ContextCompat.getColor(mainActivity, R.color.white);
            buttonMale.setTextColor(color);

            buttonFemale.setBackgroundResource(R.drawable.button_shape10_normal);
            int colorFemale = ContextCompat.getColor(mainActivity, R.color.black);
            buttonFemale.setTextColor(colorFemale);
        }


        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "cardViewFemale onClick");
                gender = "female";

                buttonFemale.setBackgroundResource(R.drawable.button_shape10);
                int color = ContextCompat.getColor(mainActivity, R.color.white);
                buttonFemale.setTextColor(color);
                buttonSubmitGender.setVisibility(View.VISIBLE);

                buttonMale.setBackgroundResource(R.drawable.button_shape10_normal);
                int colorMale = ContextCompat.getColor(mainActivity, R.color.black);
                buttonMale.setTextColor(colorMale);


            }
        });

        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "cardViewMale onClick");
                gender = "male";

                buttonMale.setBackgroundResource(R.drawable.button_shape10);
                int color = ContextCompat.getColor(mainActivity, R.color.white);
                buttonMale.setTextColor(color);
                buttonSubmitGender.setVisibility(View.VISIBLE);

                buttonFemale.setBackgroundResource(R.drawable.button_shape10_normal);
                int colorFemale = ContextCompat.getColor(mainActivity, R.color.black);
                buttonFemale.setTextColor(colorFemale);

            }
        });

        buttonSubmitGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender.equals(""))
                    return;
                SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
                User user = sharedPreferencesClient.getUserInfo("user");

                user.setGender(gender);
                sharedPreferencesClient.putUserInfo("user", user);

                userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(mainActivity,"Cập nhật thành công", Toast.LENGTH_SHORT).show();

//                            mainActivity.recreate();

                            ViewPager viewPager =  mainActivity.findViewById(R.id.view_pager);
                            ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                            Fragment accountFragment = viewPagerAdapter.getItem(3);
                            accountFragment.onResume();

                            mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.GONE);
                            mainActivity.findViewById(R.id.view_pager).setVisibility(View.VISIBLE);

                            Log.e("TAG", user.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });

            }
        });

        buttonCancelGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.GONE);
                mainActivity.findViewById(R.id.view_pager).setVisibility(View.VISIBLE);
            }
        });
    }

}