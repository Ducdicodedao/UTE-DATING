package com.client.utedating.fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NameFragment extends Fragment {
    AppCompatButton buttonConfirm;
    TextView textView, textViewStep;
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

        User user = MySharedPreferences.getUserInfo(getActivity(),"user");
        textViewStep = view.findViewById(R.id.textViewStep);
        textView = view.findViewById(R.id.textView);
        textView.setText("Xin chào "+ user.getName());

        Shader shader = new LinearGradient(0,0,0,textViewStep.getLineHeight(),
                getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT);
        textViewStep.getPaint().setShader(shader);

        buttonConfirm = view.findViewById(R.id.buttonConfirm);
        editText = view.findViewById(R.id.editTextName);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().equals("")) {
                    buttonConfirm.setEnabled(true);
                } else {
                    buttonConfirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        initialActivity = (InitialActivity) getActivity();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals(""))
                    return;

                user.setName(editText.getText().toString().trim());
                MySharedPreferences.putUserInfo(getActivity(),"user", user);
                initialActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, BirthdayFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();
            }
        });
    }
}