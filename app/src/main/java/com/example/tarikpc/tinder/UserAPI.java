package com.example.tarikpc.tinder;

import com.example.tarikpc.tinder.model.FeedInfo;
import com.example.tarikpc.tinder.model.User;
import com.example.tarikpc.tinder.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tarik PC on 06-03-18.
 */

public interface UserAPI {
    @POST("account/{action}")
    Call<UserInfo> loginUser(@Path("action") String action, @Body User user);
    @GET("statements")
    Call<FeedInfo> getFeed(@Header("Authorization") String token, @Query("page") int page);
    //Call<FeedInfo> getFeed(@Header("Authorization") String token);
}
