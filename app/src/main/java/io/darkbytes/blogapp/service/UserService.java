package io.darkbytes.blogapp.service;

import java.util.List;

import io.darkbytes.blogapp.entity.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserService {
    @GET("users")
    Call<List<UserResponse>> getUsers(@Header("Authorization") String bearerAuthorization);

    @GET("users/{id}")
    Call<UserResponse> getUser(@Path("id") Integer id, @Header("Authorization") String bearerAuthorization);

    @GET("users/me")
    Call<UserResponse> getMe(@Header("Authorization") String bearerAuthorization);
}
