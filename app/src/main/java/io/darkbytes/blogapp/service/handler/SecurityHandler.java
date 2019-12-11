package io.darkbytes.blogapp.service.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;

import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.entity.request.LoginRequest;
import io.darkbytes.blogapp.entity.request.RegisterRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.LoginResponse;
import io.darkbytes.blogapp.entity.response.LogoutResponse;
import io.darkbytes.blogapp.entity.response.RegisterResponse;
import io.darkbytes.blogapp.service.BlogApiService;
import io.darkbytes.blogapp.service.SecurityService;
import io.darkbytes.blogapp.util.JsonToObjectConverter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurityHandler {
    private SecurityService securityService = BlogApiService.getInstance().securityService();

    public void login(View view, LoginRequest request, Handler handler) {

        Call<LoginResponse> response = securityService.login(request);
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    LoginResponse login = response.body();

                    Toast.makeText(view.getContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                    SharedPreferences prefs = view.getContext().getSharedPreferences(CredentialConstant.TOKEN, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(CredentialConstant.TOKEN, login.getToken());
                    editor.putString(CredentialConstant.BEARER, String.format("%s %s", "Bearer", login.getToken()));
                    editor.putString(CredentialConstant.NAME, login.getName());
                    editor.putInt(CredentialConstant.USER_ID, login.getId());
                    editor.putString(CredentialConstant.EMAIL, login.getEmail());
                    editor.commit();

                    handler.success(response.body());

                } else {
                    Snackbar.make(view, "Invalid Email or Password", Snackbar.LENGTH_LONG).show();
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Snackbar.make(view, "Invalid Email or Password", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void register(View view, RegisterRequest request, Handler handler) {

        Call<RegisterResponse> response = securityService.register(request);
        response.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {

                    Snackbar.make(view, "Account Created Successfully", Snackbar.LENGTH_LONG).show();
                    handler.success(response.body());
                } else {
                    try {
                        ErrorResponse errorResponse = new JsonToObjectConverter<ErrorResponse>()
                                .convertToObject(response.errorBody().string(), ErrorResponse.class);

                        Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                    }
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                Snackbar.make(view, "Something happen try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void logout(View view, String bearer, Handler handler) {

        Call<LogoutResponse> response = securityService.logout(bearer);
        response.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

                    Snackbar.make(view, "User logout successfully!", Snackbar.LENGTH_LONG).show();
                    handler.success(response.body());
                } else {
                    try {
                        ErrorResponse errorResponse = new JsonToObjectConverter<ErrorResponse>()
                                .convertToObject(response.errorBody().string(), ErrorResponse.class);

                        Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                    }
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

                Snackbar.make(view, "Something happen try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }
}
