package io.darkbytes.blogapp.service;

import io.darkbytes.blogapp.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogApiService {

    private static BlogApiService instance = null;
    private Retrofit retrofit;


    private BlogApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public SecurityService securityService() {
        return retrofit.create(SecurityService.class);
    }

    public UserService userService() {
        return retrofit.create(UserService.class);
    }

    public PostService postService() {
        return retrofit.create(PostService.class);
    }

    public static BlogApiService getInstance() {
        if (instance == null)
            instance = new BlogApiService();

        return instance;
    }
}
