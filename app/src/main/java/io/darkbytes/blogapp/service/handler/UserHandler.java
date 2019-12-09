package io.darkbytes.blogapp.service.handler;

import java.net.HttpURLConnection;

import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.UserResponse;
import io.darkbytes.blogapp.service.BlogApiService;
import io.darkbytes.blogapp.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHandler {
    private UserService userService = BlogApiService.getInstance().userService();

    public void getUser(Integer id, String bearer, Handler handler) {
        Call<UserResponse> response = this.userService.getUser(id, bearer);
        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    handler.success(response.body());
                } else {
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }
}
