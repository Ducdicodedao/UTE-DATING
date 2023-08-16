package com.client.utedating.retrofit;

import com.client.utedating.models.SignUpModel;
import com.client.utedating.models.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthApiService {
    String route ="auth";
    @POST(route+"/signup")
    @FormUrlEncoded
//    Call<UserModel> signup(@Field("name") String name, @Field("email") String email, @Field("avatar") String avatar, @Field("token") String token);
    Call<SignUpModel> signup(@Field("name") String name, @Field("email") String email, @Field("avatar") String avatar, @Field("token") String token);
}
