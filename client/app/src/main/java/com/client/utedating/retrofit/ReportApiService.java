package com.client.utedating.retrofit;

import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.Report;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReportApiService {
    String route ="report";

    @POST(route+"/sendReport")
    Call<NoResultModel> sendReport(@Body Map<String, String> body);

    @GET(route+"/checkReport/{userId}")
    Call<Report> checkReport(@Path("userId") String userId);

}
