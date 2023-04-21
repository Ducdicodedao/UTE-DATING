package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;

public class GenderFragment extends Fragment {
    Button buttonFemale, buttonMale;
    Button buttonSubmitGender;

    InitialActivity initialActivity;
    String gender = "";

    public GenderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonFemale = view.findViewById(R.id.buttonFemale);
        buttonMale = view.findViewById(R.id.buttonMale);
        buttonSubmitGender = view.findViewById(R.id.buttonSubmitDatewith);

        initialActivity = (InitialActivity) getActivity();

        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "cardViewFemale onClick");
                gender = "female";
                initialActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonFemale.setBackgroundResource(R.drawable.button_shape10);
                        int color = ContextCompat.getColor(initialActivity, R.color.white);
                        buttonFemale.setTextColor(color);
                        buttonSubmitGender.setBackgroundResource(R.drawable.button_shape50);

                        buttonMale.setBackgroundResource(R.drawable.button_shape10_normal);
                        int colorMale = ContextCompat.getColor(initialActivity, R.color.black);
                        buttonMale.setTextColor(colorMale);
                    }
                });

            }
        });

        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "cardViewMale onClick");
                gender = "male";
                initialActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonMale.setBackgroundResource(R.drawable.button_shape10);
                        int color = ContextCompat.getColor(initialActivity, R.color.white);
                        buttonMale.setTextColor(color);
                        buttonSubmitGender.setBackgroundResource(R.drawable.button_shape50);

                        buttonFemale.setBackgroundResource(R.drawable.button_shape10_normal);
                        int colorFemale = ContextCompat.getColor(initialActivity, R.color.black);
                        buttonFemale.setTextColor(colorFemale);
                    }
                });
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

                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, DatewithFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();

                Log.e("TAG", user.toString());
            }
        });
    }


}