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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarFragment extends Fragment {
    ImageView imageViewAvatar;
    EditText editTextAbout;
    TextInputLayout textInputLayoutAbout;
    Button buttonSubmitAvatar;
    FrameLayout frameLayoutAddAvatar;
    Integer ADD_AVATAR = 2;
    Uri imgUri ;
    InitialActivity initialActivity;
    StorageReference mStorageReference;
    UserApiService userApiService;
    public AvatarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avatar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        editTextAbout = view.findViewById(R.id.editTextAbout);
        textInputLayoutAbout = view.findViewById(R.id.textInputLayoutAbout);
        frameLayoutAddAvatar = view.findViewById(R.id.frameLayoutAddAvatar);
        buttonSubmitAvatar = view.findViewById(R.id.buttonSubmitAvatar);

        initialActivity = (InitialActivity) getActivity();
        mStorageReference = FirebaseStorage.getInstance("gs://ute-dating.appspot.com").getReference("avatar");

        userApiService = RetrofitClient.getInstance().create(UserApiService.class);

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        User user = sharedPreferencesClient.getUserInfo("user");

        Glide
                .with(view)
                .load(user.getAvatar())
                .centerCrop()
                .into(imageViewAvatar);

        frameLayoutAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAvatar();
            }
        });

        buttonSubmitAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAvatar(view);
            }
        });

    }

    private void submitAvatar(View view) {
        if(editTextAbout.getText().toString().trim().equals("")){
            Toast.makeText(initialActivity,"Bạn chưa giới thiệu bản thân", Toast.LENGTH_LONG ).show();
            return;
        }
        if(editTextAbout.getText().toString().trim().length() > 50){
            Toast.makeText(initialActivity,"Só lượng ký tự vượt quá 50", Toast.LENGTH_LONG ).show();
            return;
        }
        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        User user = sharedPreferencesClient.getUserInfo("user");

        final  StorageReference storageReference = mStorageReference.child(user.getEmail()+"."+getfileextension(imgUri));
        storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uriImage) {
                        user.setAvatar(String.valueOf(uriImage));
                        user.setAbout(editTextAbout.getText().toString().trim());
                        sharedPreferencesClient.putUserInfo("user", user);
                        Log.e("TAG", user.toString());

                        userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if(response.isSuccessful()){
                                    initialActivity.getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentContainerView, WelcomeFragment.class, null)
                                            .setReorderingAllowed(true)
                                            .addToBackStack("name") // name can be null
                                            .commit();

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



    }

    private void addAvatar() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, ADD_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_AVATAR && resultCode  == RESULT_OK && data.getData() != null ){
            imgUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(initialActivity.getContentResolver(),imgUri);
                imageViewAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  String getfileextension(Uri audioUri){
        ContentResolver contentResolver = initialActivity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

}