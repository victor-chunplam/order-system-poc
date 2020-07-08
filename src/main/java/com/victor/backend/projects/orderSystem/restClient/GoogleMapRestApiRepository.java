package com.victor.backend.projects.orderSystem.restClient;

import com.victor.backend.projects.orderSystem.pojo.restClient.resp.GoogleMapDistanceMatrixResp;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface GoogleMapRestApiRepository {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("distancematrix/json")
    Call<GoogleMapDistanceMatrixResp> postDistanceMatrix(
            @QueryMap Map<String, Object> urlParams);
}
