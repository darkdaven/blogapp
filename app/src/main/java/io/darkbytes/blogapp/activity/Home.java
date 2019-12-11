package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.adapter.PostAdapter;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.service.websocket.HomeEventListener;
import io.darkbytes.blogapp.service.websocket.WebSocketService;
import io.darkbytes.blogapp.util.PreferenceUtil;

public class Home extends AppCompatActivity {

    private String bearer;
    private String token;
    private Integer userId;
    private List<PostResponse> postResponses = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
    private PostHandler postHandler = new PostHandler();
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView myPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bearer = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.BEARER);
        token = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.TOKEN);
        userId = PreferenceUtil.getInt(getApplicationContext(), CredentialConstant.USER_ID);

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

        myPost = findViewById(R.id.post_home_my_post_button);
        myPost.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), User.class);
            intent.putExtra(Constant.USER_ID, userId);
            startActivity(intent);
        });

        postAdapter = new PostAdapter(this.postResponses, this);

        postRecyclerView = findViewById(R.id.post_home_recicler_view);
        postRecyclerView.setAdapter(postAdapter);

        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WebSocketService.getInstance(token).subscribe(new HomeEventListener(this, postRecyclerView, postAdapter, postResponses));
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
}