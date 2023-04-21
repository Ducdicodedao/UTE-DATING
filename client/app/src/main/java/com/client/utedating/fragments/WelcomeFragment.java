package com.client.utedating.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.activities.MainActivity;

public class WelcomeFragment extends Fragment {
    Button buttonSubmitWelcome;
    InitialActivity initialActivity;
    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonSubmitWelcome = view.findViewById(R.id.buttonSubmitWelcome);
        initialActivity = (InitialActivity) getActivity();

        buttonSubmitWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(initialActivity, MainActivity.class);
                startActivity(i);
                initialActivity.finish();
            }
        });
    }
}