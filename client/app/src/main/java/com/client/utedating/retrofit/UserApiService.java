package com.client.utedating.retrofit;

import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {
    String route ="user";

    @PATCH(route+"/updateInfo/{userId}")
    @FormUrlEncoded
    Call<UserModel> updateName(@Path("userId") String userId, @Field("name") String name);

    @PATCH(route+"/updateInfo/{userId}")
    Call<UserModel> updateInfo(@Path("userId") String userId, @Body User user);
}
