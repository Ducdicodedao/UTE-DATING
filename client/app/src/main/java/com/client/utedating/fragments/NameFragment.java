package com.client.utedating.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NameFragment extends Fragment {
    FloatingActionButton floating_button_SubmitName;
    EditText editText;
    InitialActivity initialActivity;

    UserApiService userApiService;

    public NameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        User user = sharedPreferencesClient.getUserInfo("user");

        floating_button_SubmitName = view.findViewById(R.id.floating_button_SubmitName);
        editText = view.findViewById(R.id.editTextName);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().equals("")) {
//                    floating_button_GoToBirthDay.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.black));
                    floating_button_SubmitName.setImageResource(R.drawable.ic_baseline_chevron_right_24_white);
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        initialActivity = (InitialActivity) getActivity();
        floating_button_SubmitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals(""))
                    return;

                user.setName(editText.getText().toString().trim());
                SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
                sharedPreferencesClient.putUserInfo("user", user);
                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, BirthdayFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();
//                userApiService.updateName(user.get_id(), editText.getText().toString().trim()).enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        initialActivity.getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragmentContainerView, BirthdayFragment.class, null)
//                                .setReorderingAllowed(true)
//                                .addToBackStack("name") // name can be null
//                                .commit();
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//
//                    }
//                });

            }
        });
    }
}