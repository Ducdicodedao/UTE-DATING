package com.client.utedating.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.activities.LoginGGActivity;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    ImageView imageViewAvatar;
    FrameLayout frameLayoutAddAvatar;
    TextView textViewName, textViewCountAbout, textViewSaveAbout;
    ImageView imageViewAuth, imageViewNotAuth;
    EditText editTextAbout;
    Button buttonFemale, buttonMale, buttonDatewithFemale, buttonDatewithMale;
    ChipGroup chipGroupFaculty,chipGroupInterest;
    Chip chipFaculty;
    Button buttonLogout;

    MainActivity mainActivity;

    UserApiService userApiService;
    User user;

    Integer ADD_AVATAR = 2;
    Uri imgUri ;

    private FirebaseAuth mAuth;
    SharedPreferencesClient sharedPreferencesClient;
    StorageReference mStorageReference;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchData();
        setData(view);
        handleEvent();
    }

    private void handleEvent() {
        frameLayoutAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAvatar();
            }
        });
        editTextAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lấy số ký tự đang nhập
                int length = s.toString().length();
                // Làm gì đó với số ký tự
                textViewCountAbout.setText(length+"/200");
                if(length <= 200){
                    textViewSaveAbout.setVisibility(View.VISIBLE);
                }
                else{
                    textViewSaveAbout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textViewSaveAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveAbout();
            }
        });
        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGender();
            }
        });
        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGender();
            }
        });
        buttonDatewithFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatewith();
            }
        });
        buttonDatewithFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatewith();
            }
        });
        chipFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFaculty();
            }
        });
        for (int i = 0; i < chipGroupInterest.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupInterest.getChildAt(i);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleInterest();
                }
            });
        }
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });
    }


    private void addAvatar() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, ADD_AVATAR);
    }

    private void handleSaveAbout() {
        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(mainActivity);
        user.setAbout(editTextAbout.getText().toString().trim());
        sharedPreferencesClient.putUserInfo("user", user);

        textViewSaveAbout.setVisibility(View.INVISIBLE);

        userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(mainActivity, "Cập nhật mô tả thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }


    private void handleLogout() {
        mAuth.signOut();
        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(mainActivity);
        sharedPreferencesClient.putUserInfo("user", null);
        Intent i = new Intent(mainActivity, LoginGGActivity.class);
        startActivity(i);
        mainActivity.finish();
    }

    private void handleInterest() {
        Fragment fragment = new InterestUpdateFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame_Layout, fragment)
                .commit();

        mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.view_pager).setVisibility(View.GONE);
    }


    private void handleFaculty() {
        Fragment fragment = new FacultyUpdateFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame_Layout, fragment)
                .commit();

        mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.view_pager).setVisibility(View.GONE);
    }

    private void handleDatewith() {
        Fragment fragment = new DatewithUpdateFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame_Layout, fragment)
                .commit();

        mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.view_pager).setVisibility(View.GONE);
    }


    private void handleGender() {
        Fragment fragment = new GenderUpdateFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame_Layout, fragment)
                .commit();

        mainActivity.findViewById(R.id.frame_Layout).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.view_pager).setVisibility(View.GONE);
    }

    private void setData(View view) {
        Glide
                .with(mainActivity)
                .load(user.getAvatar())
                .centerCrop()
                .into(imageViewAvatar);

        textViewName.setText(user.getName());
        if(user.getAuthenticated()){
            imageViewAuth.setVisibility(View.VISIBLE);
            imageViewNotAuth.setVisibility(View.GONE);
        }
        editTextAbout.setText(user.getAbout());
        textViewCountAbout.setText(user.getAbout().length()+"/200");
        if(user.getGender().equals("male")){
            buttonMale.setBackgroundResource(R.drawable.button_shape10_outline);
            buttonMale.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else if(user.getGender().equals("female")){
            buttonFemale.setBackgroundResource(R.drawable.button_shape10_outline);
            buttonFemale.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if(user.getDateWith().equals("male")){
            buttonDatewithMale.setBackgroundResource(R.drawable.button_shape10_outline);
            buttonDatewithMale.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else if(user.getDateWith().equals("female")){
            buttonDatewithFemale.setBackgroundResource(R.drawable.button_shape10_outline);
            buttonDatewithFemale.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        chipFaculty = new Chip(mainActivity);
        chipFaculty.setText(user.getFaculty());
        chipFaculty.setTextColor(getResources().getColor(R.color.colorPrimary));
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(mainActivity,
                null,
                0,
                R.style.ChipChoice);
        chipFaculty.setChipDrawable(chipDrawable);
        chipFaculty.setChecked(true);
        chipGroupFaculty.addView(chipFaculty);

        for (int i = 0; i < chipGroupInterest.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupInterest.getChildAt(i);
            String chipText = chip.getText().toString();
            int flag = 0;
            Log.d("ChipText", chipText);
            for(int j = 0; j < user.getInterests().size(); j++){
                if(chipText.equals(user.getInterests().get(j))){
                    flag = 1;
                }
            }
            if(flag == 0){
                chip.setVisibility(View.GONE);
            }

        }
    }

    private void fetchData() {
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        sharedPreferencesClient = new SharedPreferencesClient(mainActivity);
        user = sharedPreferencesClient.getUserInfo("user");

        userApiService.getInfo(user.get_id()).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful())
                {
                    user = response.body().getResult();
                    sharedPreferencesClient.putUserInfo("key", user);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }

    private void initView(View view) {
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        frameLayoutAddAvatar = view.findViewById(R.id.frameLayoutAddAvatar);
        textViewName = view.findViewById(R.id.textViewName);
        imageViewAuth = view.findViewById(R.id.imageViewAuth);
        imageViewNotAuth = view.findViewById(R.id.imageViewNotAuth);
        editTextAbout = view.findViewById(R.id.editTextAbout);
        textViewCountAbout = view.findViewById(R.id.textViewCountAbout);
        textViewSaveAbout = view.findViewById(R.id.textViewSaveAbout);
        buttonFemale = view.findViewById(R.id.buttonFemale);
        buttonMale = view.findViewById(R.id.buttonMale);
        buttonDatewithFemale = view.findViewById(R.id.buttonDatewithFemale);
        buttonDatewithMale = view.findViewById(R.id.buttonDatewithMale);
        chipGroupFaculty = view.findViewById(R.id.chipGroupFaculty);
        chipGroupInterest = view.findViewById(R.id.chipGroupInterest);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        mainActivity = (MainActivity) getActivity();
        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance("gs://ute-dating.appspot.com").getReference("avatar");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_AVATAR && resultCode  == RESULT_OK && data.getData() != null ){
            imgUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(),imgUri);
                imageViewAvatar.setImageBitmap(bitmap);

                final StorageReference storageReference = mStorageReference.child(user.getEmail()+"."+getfileextension(imgUri));
                storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uriImage) {
                                user.setAvatar(String.valueOf(uriImage));
                                sharedPreferencesClient.putUserInfo("user", user);

                                userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                                    @Override
                                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                        if(response.isSuccessful()){
                                            Log.e("TAG",response.body().getResult().toString());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<UserModel> call, Throwable t) {
                                        Log.e("TAG", t.getMessage());
                                    }
                                });
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  String getfileextension(Uri audioUri){
        ContentResolver contentResolver = mainActivity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

}