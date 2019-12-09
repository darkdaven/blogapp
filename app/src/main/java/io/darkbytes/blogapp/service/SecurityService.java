package io.darkbytes.blogapp.service;


import io.darkbytes.blogapp.entity.request.LoginRequest;
import io.darkbytes.blogapp.entity.request.RegisterRequest;
import io.darkbytes.blogapp.entity.response.LoginResponse;
import io.darkbytes.blogapp.entity.response.LogoutResponse;
import io.darkbytes.blogapp.entity.response.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SecurityService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @DELETE("logout")
    Call<LogoutResponse> logout(@Header("Authorization") String bearerAuthorization);

}