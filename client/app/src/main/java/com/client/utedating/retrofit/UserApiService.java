package com.client.utedating.retrofit;

import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.models.UsersLikedModel;
import com.client.utedating.models.UsersModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {
    String route ="user";

    @PATCH(route+"/updateInfo/{userId}")
    Call<UserModel> updateInfo(@Path("userId") String userId, @Body User user);

    @GET(route+"/getInfo/{userId}")
    Call<UserModel> getInfo(@Path("userId") String userId);

    @GET(route+"/getUsersByDatewith/{userId}")
    Call<UsersModel> getUsersByDatewith(@Path("userId") String userId);

    @GET(route+"/isUserSwipedRight/{userId}/{swipedUserId}")
    Call<NoResultModel> isUserSwipedRight(@Path("userId") String userId, @Path("swipedUserId") String swipedUserId);

    @PATCH(route+"/addUserMatched")
    Call<NoResultModel> addUserMatched(@Body Map<String, String> body);

    @PATCH(route+"/addUserSwipedRight")
    Call<NoResultModel> addUserSwipedRight(@Body Map<String, String> body);

    @GET(route+"/getUserSwipedRight/{userId}")
    Call<UsersLikedModel> getUserSwipedRight(@Path("userId") String userId);

    @GET(route+"/getUserMatched/{userId}")
    Call<UsersLikedModel> getUserMatched(@Path("userId") String userId);

    @GET(route+"/isUserSwipedRight/{userId}/{swipedUserId}")
    Call<List<String>> getUsersAvatar(@Path("userId") String userId, @Path("swipedUserId") String swipedUserId);
}
