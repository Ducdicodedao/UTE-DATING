package com.client.utedating.fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import android.widget.TextView;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.utils.MySharedPreferences;

public class DatewithFragment extends Fragment {
    Button buttonFemale, buttonMale;
    Button buttonSubmitDatewith;
    TextView textViewStep;
    InitialActivity initialActivity;
    String gender = "";

    public DatewithFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_datewith, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonFemale = view.findViewById(R.id.buttonFemale);
        buttonMale = view.findViewById(R.id.buttonMale);
        buttonSubmitDatewith = view.findViewById(R.id.buttonSubmitDatewith);
        textViewStep = view.findViewById(R.id.textViewStep);
        Shader shader = new LinearGradient(0,0,0,textViewStep.getLineHeight(),
                getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT);
        textViewStep.getPaint().setShader(shader);
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
                        buttonSubmitDatewith.setBackgroundResource(R.drawable.button_shape50);

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
                        buttonSubmitDatewith.setBackgroundResource(R.drawable.button_shape50);

                        buttonFemale.setBackgroundResource(R.drawable.button_shape10_normal);
                        int colorFemale = ContextCompat.getColor(initialActivity, R.color.black);
                        buttonFemale.setTextColor(colorFemale);
                    }
                });
            }
        });

        buttonSubmitDatewith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender.equals(""))
                    return;
                User user = MySharedPreferences.getUserInfo(getActivity(), "user");

                user.setDateWith(gender);
                MySharedPreferences.putUserInfo(getActivity(), "user", user);

                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, FacultyFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();

                Log.e("TAG", user.toString());
            }
        });
    }
}