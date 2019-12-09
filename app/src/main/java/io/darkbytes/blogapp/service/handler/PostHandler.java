package io.darkbytes.blogapp.service.handler;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.net.HttpURLConnection;
import java.util.List;

import io.darkbytes.blogapp.entity.request.PostCommentRequest;
import io.darkbytes.blogapp.entity.request.PostRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostCommentResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.BlogApiService;
import io.darkbytes.blogapp.service.PostService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostHandler {
    private PostService postService = BlogApiService.getInstance().postService();

    public void addOrRemoveLike(PostResponse post, String bearer, Handler handler) {
        Call<ResponseBody> response;
        if (post.getLiked() == null)
            return;

        if (post.getLiked()) {
            response = postService.deleteLikePost(post.getId(), bearer);
        } else {
            response = postService.likePost(post.getId(), bearer);
        }

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    getPost(post.getId(), bearer, handler);
                } else {
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void getPost(Integer id, String bearer, Handler handler) {
        Call<PostResponse> response = this.postService.getPost(id, bearer);
        response.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    handler.success(response.body());
                } else {
                    handler.failure(ErrorResponse.fromErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void getPosts(View view, String bearer, Handler handler) {

        Call<List<PostResponse>> response = postService.getPosts(bearer);
        response.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    handler.success(response.body());

                } else {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(response.errorBody());
                    Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {

                Snackbar.make(view, "Something happen, try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void getPostsByUser(View view, Integer userId, String bearer, Handler handler) {

        Call<List<PostResponse>> response = postService.getPostByUser(userId ,bearer);
        response.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    handler.success(response.body());

                } else {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(response.errorBody());
                    Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {

                Snackbar.make(view, "Something happen, try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void savePost(View view, PostRequest request, String bearer, Handler handler) {
        Call<PostResponse> response = postService.createPost(request, bearer);
        response.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    handler.success(response.body());
                } else {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(response.errorBody());
                    Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

                Snackbar.make(view, "Something happen, try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void getComments(Integer id, View view, String bearer, Handler handler) {
        Call<List<PostCommentResponse>> response = postService.getPostComments(id, bearer);
        response.enqueue(new Callback<List<PostCommentResponse>>() {
            @Override
            public void onResponse(Call<List<PostCommentResponse>> call, Response<List<PostCommentResponse>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    handler.success(response.body());
                } else {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(response.errorBody());
                    Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                    handler.failure(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<List<PostCommentResponse>> call, Throwable t) {
                Snackbar.make(view, "Something happen, try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }

    public void savePostComment(Integer id, View view, PostCommentRequest request, String bearer, Handler handler) {
        Call<PostCommentResponse> response = postService.createPostComment(id, request, bearer);
        response.enqueue(new Callback<PostCommentResponse>() {
            @Override
            public void onResponse(Call<PostCommentResponse> call, Response<PostCommentResponse> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    handler.success(response.body());
                } else {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(response.errorBody());
                    Snackbar.make(view, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                    handler.failure(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponse> call, Throwable t) {
                Snackbar.make(view, "Something happen, try again", Snackbar.LENGTH_LONG).show();
                handler.failure(new ErrorResponse(true, t.getMessage()));
            }
        });
    }
}
