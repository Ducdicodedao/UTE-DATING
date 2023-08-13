package com.client.utedating.retrofit;

import com.client.utedating.models.ConversationModel;
import com.client.utedating.models.MessageModel;
import com.client.utedating.models.NoResultModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConversationApiService {
    String route = "conversation";

    @POST(route+"/createConversation")
    Call<NoResultModel> createConversation(@Body Map<String, String> body);

    @POST(route+"/sendMessage/{conversationId}")
    Call<NoResultModel> sendMessage(@Path("conversationId") String conversationId, @Body Map<String, String> body);

    @GET(route+"/getConversationIdByUserId/{senderId}/{receiverId}")
    Call<String> getConversationIdByUserId(@Path("senderId") String senderId, @Path("receiverId") String receiverId);

    @GET(route+"/getMessages/{conversationId}")
    Call<MessageModel> getMessages(@Path("conversationId") String conversationId);

    @GET(route+"/getMoreMessages/{conversationId}")
    Call<MessageModel> getMoreMessages(@Path("conversationId") String conversationId, @Query("page") Integer page);

    @GET(route+"/getConversationsByUserId/{userId}")
    Call<ConversationModel> getConversationByUserId(@Path("userId") String userId);

    @GET(route+"/getUserMatched/{userId}")
    Call<ConversationModel> getUserMatched(@Path("userId") String userId);

    @GET(route+"/isExist/{conversationId}")
    Call<NoResultModel> isExist(@Path("conversationId") String conversationId);
}
