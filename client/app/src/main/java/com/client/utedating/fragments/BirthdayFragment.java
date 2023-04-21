package com.client.utedating.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.DateTimeException;
import java.time.LocalDate;

public class BirthdayFragment extends Fragment {
    EditText editTextDay, editTextMonth, editTextYear;
    FloatingActionButton floating_button_SubmitBirthday;
    UserApiService userApiService;
    InitialActivity initialActivity;
    public BirthdayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_birthday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        editTextDay = view.findViewById(R.id.editTextDay);
        editTextMonth = view.findViewById(R.id.editTextMonth);
        editTextYear = view.findViewById(R.id.editTextYear);

        floating_button_SubmitBirthday = view.findViewById(R.id.floating_button_SubmitBirthday);
        initialActivity = (InitialActivity) getActivity();

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        User user = sharedPreferencesClient.getUserInfo("user");

        floating_button_SubmitBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(editTextDay.getText().toString());
                int b = Integer.parseInt(editTextMonth.getText().toString());
                int c = Integer.parseInt(editTextYear.getText().toString());

                boolean isValid = true;
                try {
                    LocalDate date = LocalDate.of(c, b, a);
                    Log.e("TAG",date.toString());
                } catch (DateTimeException e) {
                    isValid = false;
                    Log.e("TAG","Ngày tháng năm không hợp lệ");
                }

                if (!isValid) {
                    Toast.makeText(initialActivity,"Ngày sinh không hợp lệ", Toast.LENGTH_LONG).show();
                    return;
                }

                user.setBirthday(editTextDay.getText().toString()+"/"+editTextMonth.getText().toString()+"/"+editTextYear.getText().toString());
                SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
                sharedPreferencesClient.putUserInfo("user", user);

                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, GenderFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();

                Log.e("TAG", user.toString());
            }
            });
        }
    }
