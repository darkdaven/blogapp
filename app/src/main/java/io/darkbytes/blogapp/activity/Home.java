package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.adapter.PostAdapter;
import io.darkbytes.blogapp.entity.event.LikePostEvent;
import io.darkbytes.blogapp.entity.event.NewCommentEvent;
import io.darkbytes.blogapp.entity.event.NewPostEvent;
import io.darkbytes.blogapp.entity.event.UserConnectedEvent;
import io.darkbytes.blogapp.entity.event.UserDisconnectEvent;
import io.darkbytes.blogapp.entity.event.ViewPostEvent;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.service.websocket.EventListener;
import io.darkbytes.blogapp.service.websocket.WebSocketService;
import io.darkbytes.blogapp.utit.PreferenceUtil;

public class Home extends AppCompatActivity {

    private String bearer;
    private String token;
    private List<PostResponse> postResponses = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
    private PostHandler postHandler = new PostHandler();
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bearer = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.BEARER);
        token = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.TOKEN);

        shimmerFrameLayout = findViewById(R.id.post_shimmer_view_containet);
        swipeRefreshLayout = findViewById(R.id.home_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPost();
            swipeRefreshLayout.setRefreshing(false);
        });

        FloatingActionButton fab = findViewById(R.id.home_new_post_btn);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CreatePost.class);
            startActivity(intent);
        });

        postAdapter = new PostAdapter(this.postResponses, this);

        postRecyclerView = findViewById(R.id.post_home_recicler_view);
        postRecyclerView.setAdapter(postAdapter);

        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handleWebSocketEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getPost();
    }

    @Override
    protected void onPause() {
        this.shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    private void getPost() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        postHandler.getPosts(this.postRecyclerView, bearer, new Handler<List<PostResponse>>() {
            @Override
            public void success(List<PostResponse> response) {
                postResponses.clear();
                postResponses.addAll(response);
                postAdapter.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void failure(ErrorResponse errorBody) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void handleWebSocketEvent() {
        WebSocketService.getInstance(token).subscribe(new EventListener() {
            public void onLikePostEvent(LikePostEvent event) {

                runOnUiThread(() -> {
                    Optional<PostResponse> existPost = postResponses
                            .stream()
                            .filter(p -> p.getId().intValue() == event.getPostId().intValue())
                            .findFirst();

                    if (existPost.isPresent()) {
                        PostResponse post = existPost.get();
                        post.setLikes(event.getLikes());
                        updateUiPost(post);
                    }
                });
            }

            @Override
            public void onViewPostEvent(ViewPostEvent event) {
                runOnUiThread(() -> {
                    Optional<PostResponse> existPost = postResponses
                            .stream()
                            .filter(p -> p.getId().intValue() == event.getPostId().intValue())
                            .findFirst();

                    if (existPost.isPresent()) {
                        PostResponse post = existPost.get();
                        post.setViews(event.getViews());
                        updateUiPost(post);
                    }
                });
            }

            @Override
            public void onNewCommentEvent(NewCommentEvent event) {
                runOnUiThread(() -> {
                    Optional<PostResponse> existPost = postResponses
                            .stream()
                            .filter(p -> p.getId().intValue() == event.getPostId().intValue())
                            .findFirst();

                    if (existPost.isPresent()) {
                        PostResponse post = existPost.get();
                        post.setComments(event.getComments());
                        updateUiPost(post);


                        Snackbar.make(postRecyclerView, String.format("%s add a comment", event.getUserName()), Snackbar.LENGTH_LONG)
                                .setAction("View Post", v -> {
                                    Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                    intent.putExtra(Constant.POST, new PostResponse(event.getPostId()));
                                    startActivity(intent);
                                }).show();
                    }
                });
            }

            @Override
            public void onNewPostEvent(NewPostEvent event) {
                runOnUiThread(() -> {
                    PostResponse post = PostResponse.fromNewPostEvent(event);
                    postResponses.add(post);
                    postAdapter.notifyItemInserted(postResponses.size() - 1);

                    Snackbar.make(postRecyclerView, String.format("%s create new Post", event.getUserName()), Snackbar.LENGTH_LONG)
                            .setAction("View Post", v -> {
                                Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                intent.putExtra(Constant.POST, new PostResponse(event.getPost().getId()));
                                startActivity(intent);
                            }).show();
                });
            }

            @Override
            public void onUserConnected(UserConnectedEvent event) {
                Snackbar.make(postRecyclerView,
                        String.format("%s is now connected", event.getUserEmail()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                Snackbar.make(postRecyclerView,
                        String.format("%s disconnect", event.getUserEmail()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void updateUiPost(PostResponse post) {
        int index = postResponses.indexOf(post);
        postAdapter.notifyItemChanged(index);
    }
}