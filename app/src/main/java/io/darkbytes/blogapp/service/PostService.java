package io.darkbytes.blogapp.service;

import java.util.List;

import io.darkbytes.blogapp.entity.request.PostCommentRequest;
import io.darkbytes.blogapp.entity.request.PostRequest;
import io.darkbytes.blogapp.entity.response.PostCommentResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostService {
    @GET("post")
    Call<List<PostResponse>> getPosts(@Header("Authorization") String bearerAuthorization);

    @GET("post/{id}")
    Call<PostResponse> getPost(@Path("id") Integer id, @Header("Authorization") String bearerAuthorization);

    @GET("post")
    Call<List<PostResponse>> getPostByUser(@Query("userId") Integer userId, @Header("Authorization") String bearerAuthorization);

    @POST("post")
    Call<PostResponse> createPost(@Body PostRequest request, @Header("Authorization") String bearerAuthorization);

    @GET("post/{id}/comment")
    Call<List<PostCommentResponse>> getPostComments(@Path("id") Integer id, @Header("Authorization") String bearerAuthorization);

    @POST("post/{id}/comment")
    Call<PostCommentResponse> createPostComment(@Path("id") Integer id, @Body PostCommentRequest request, @Header("Authorization") String bearerAuthorization);

    @PUT("post/{id}/like")
    Call<ResponseBody> likePost(@Path("id") Integer id, @Header("Authorization") String bearerAuthorization);

    @DELETE("post/{id}/like")
    Call<ResponseBody> deleteLikePost(@Path("id") Integer id, @Header("Authorization") String bearerAuthorization);
}
