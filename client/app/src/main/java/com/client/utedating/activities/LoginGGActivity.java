package com.client.utedating.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.models.ReportModel;
import com.client.utedating.models.SignUpModel;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.AuthApiService;
import com.client.utedating.retrofit.ReportApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.utils.MyModal;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginGGActivity extends AppCompatActivity {
    private static final String TAG = "LoginGGActivity";
    GoogleSignInClient mGoogleSignInClient;
    AppCompatButton buttonSigninGG;
    Animation scaleAnimation;
    int RC_SIGN_IN = 100;

    AuthApiService authApiService;
    ReportApiService reportApiService;

    private FirebaseAuth mAuth;
    MyModal myModal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ggactivity);

        mAuth = FirebaseAuth.getInstance();
        authApiService = RetrofitClient.getInstance().create(AuthApiService.class);
        reportApiService = RetrofitClient.getInstance().create(ReportApiService.class);
        buttonSigninGG = findViewById(R.id.sign_in_button);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        checkIsLogIn();
        myModal = new MyModal(LoginGGActivity.this);
        buttonSigninGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSigninGG.startAnimation(scaleAnimation);
                myModal.show();
                signIn();
            }
        });
    }

    private void checkIsLogIn() {
        User mUser = MySharedPreferences.getUserInfo(LoginGGActivity.this, "user");
        FirebaseUser Fuser = mAuth.getCurrentUser();
        if (Fuser != null && !mUser.getBirthday().equals("") && !mUser.getGender().equals("") && !mUser.getDateWith().equals("") && !mUser.getAbout().equals("") && !mUser.getFaculty().equals("") && mUser.getInterests().size() != 0 && mUser.getLocation() != null) {
            Intent intent = new Intent(LoginGGActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String token) {
                                    Log.e("TAG","FToken: "+ token);
                                    assert user != null;
//                                    authApiService.signup(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), token).enqueue(new Callback<UserModel>() {
//                                        @Override
//                                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                                            if (response.isSuccessful()) {
//                                                UserModel userModel = response.body();
//                                                User mUser = userModel.getResult();
//                                                Log.e("TAG", mUser.toString());
//                                                SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(LoginGGActivity.this);
//                                                sharedPreferencesClient.putUserInfo("user", mUser);
//                                                Intent intent;
//                                                if (response.body().getMessage().equals("Signin Success")) {
//                                                    if (mUser.getBirthday().equals("") || mUser.getGender().equals("") || mUser.getDateWith().equals("") || mUser.getAbout().equals("") || mUser.getFaculty().equals("") ||mUser.getInterests().size() == 0 ||mUser.getLocation() == null) {
//                                                        intent = new Intent(LoginGGActivity.this, InitialActivity.class);
//                                                    } else {
//                                                        intent = new Intent(LoginGGActivity.this, MainActivity.class);
//                                                    }
//                                                } else {
//                                                    intent = new Intent(LoginGGActivity.this, InitialActivity.class);
//                                                }
//
//                                                reportApiService.checkReport(mUser.get_id()).enqueue(new Callback<Report>() {
//                                                    @Override
//                                                    public void onResponse(Call<Report> call, Response<Report> response) {
//                                                        if(!response.body().getTitle().equals("")){
//                                                            Dialog dialog = new Dialog(LoginGGActivity.this);
//                                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                                            dialog.setContentView(R.layout.dialog_check_report);
//                                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                            dialog.getWindow().setDimAmount(0.6f);
//                                                            dialog.getWindow().setGravity(Gravity.CENTER);
//                                                            dialog.setCanceledOnTouchOutside(false);
//                                                            dialog.show();
//
//                                                            TextView textViewReportDetail = dialog.findViewById(R.id.textViewReportDetail);
//                                                            Button btn_verifyReport = dialog.findViewById(R.id.btn_verifyReport);
//                                                            TextView btn_contactUTEDATING = dialog.findViewById(R.id.btn_contactUTEDATING);
//
//                                                            textViewReportDetail.setText(response.body().getTitle());
//                                                            btn_verifyReport.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    dialog.dismiss();
//                                                                }
//                                                            });
//                                                            btn_contactUTEDATING.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    contactUs();
//                                                                    dialog.dismiss();
//                                                                }
//                                                            });
//                                                        }
//                                                        else{
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//                                                    }
//                                                    @Override
//                                                    public void onFailure(Call<Report> call, Throwable t) {
//                                                        Log.e("TAG", t.getMessage());
//                                                    }
//                                                });
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<UserModel> call, Throwable t) {
//                                            Toast.makeText(LoginGGActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    });
                                    authApiService.signup(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), token).enqueue(new Callback<SignUpModel>() {
                                        @Override
                                        public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                                            if (response.isSuccessful()) {
                                                SignUpModel signUpModel = response.body();
                                                User mUser = signUpModel.getResult();
                                                Log.e("TAG", "JwtToken:"+ signUpModel.getJwtToken());
                                                Log.e("TAG", mUser.toString());
                                                MySharedPreferences.putUserInfo(LoginGGActivity.this, "user", mUser);
                                                MySharedPreferences.setStringSharedPreference(LoginGGActivity.this, "jwt", signUpModel.getJwtToken());
                                                Intent intent;
                                                if (response.body().getMessage().equals("Signin Success")) {
                                                    if (mUser.getBirthday().equals("") || mUser.getGender().equals("") || mUser.getDateWith().equals("") || mUser.getAbout().equals("") || mUser.getFaculty().equals("") ||mUser.getInterests().size() == 0 ||mUser.getLocation() == null) {
                                                        intent = new Intent(LoginGGActivity.this, InitialActivity.class);
                                                    } else {
                                                        intent = new Intent(LoginGGActivity.this, MainActivity.class);
                                                    }
                                                } else {
                                                    intent = new Intent(LoginGGActivity.this, InitialActivity.class);
                                                }

                                                reportApiService.checkReport(mUser.get_id()).enqueue(new Callback<ReportModel>() {
                                                    @Override
                                                    public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
                                                        if(response.body().getExist()){
                                                            myModal.dismiss();
                                                            Dialog dialog = new Dialog(LoginGGActivity.this);
                                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                            dialog.setContentView(R.layout.dialog_check_report);
                                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            dialog.getWindow().setDimAmount(0.6f);
                                                            dialog.getWindow().setGravity(Gravity.CENTER);
                                                            dialog.setCanceledOnTouchOutside(false);
                                                            dialog.show();

                                                            TextView textViewReportDetail = dialog.findViewById(R.id.textViewReportDetail);
                                                            Button btn_verifyReport = dialog.findViewById(R.id.btn_verifyReport);
                                                            TextView btn_contactUTEDATING = dialog.findViewById(R.id.btn_contactUTEDATING);

                                                            textViewReportDetail.setText(response.body().getReport().getTitle());
                                                            btn_verifyReport.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            btn_contactUTEDATING.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    contactUs();
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                        }
                                                        else{
                                                            myModal.dismiss();
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<ReportModel> call, Throwable t) {
                                                        Log.e("TAG","checkReport: "+ t.getMessage());
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SignUpModel> call, Throwable t) {
                                            Toast.makeText(LoginGGActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    //finish();
                                    //updateUI(user);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
    private void contactUs() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ducdevday@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_STREAM, "");
            emailIntent.setPackage("com.google.android.gm");
            startActivity(emailIntent);
        } catch (Resources.NotFoundException e) {
            Intent sendIntentIfGmailFail = new Intent(Intent.ACTION_SEND);
            sendIntentIfGmailFail.setType("*/*");
            sendIntentIfGmailFail.putExtra(Intent.EXTRA_EMAIL, "info@zen-s.com");
            sendIntentIfGmailFail.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            if (sendIntentIfGmailFail.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntentIfGmailFail);
            }
        }
    }
}