package com.client.utedating.fragments;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.DateTimeException;
import java.time.LocalDate;

public class BirthdayFragment extends Fragment {
    EditText editTextDay, editTextMonth, editTextYear;
    AppCompatButton buttonConfirm;
    UserApiService userApiService;
    InitialActivity initialActivity;
    TextView textViewStep;
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
        textViewStep = view.findViewById(R.id.textViewStep);
        editTextDay = view.findViewById(R.id.editTextDay);
        editTextMonth = view.findViewById(R.id.editTextMonth);
        editTextYear = view.findViewById(R.id.editTextYear);

        buttonConfirm = view.findViewById(R.id.buttonConfirm);
        initialActivity = (InitialActivity) getActivity();
        User user = MySharedPreferences.getUserInfo(getActivity(), "user");

        Shader shader = new LinearGradient(0,0,0,textViewStep.getLineHeight(),
                getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT);
        textViewStep.getPaint().setShader(shader);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
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
                MySharedPreferences.putUserInfo(getActivity(), "user", user);

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
