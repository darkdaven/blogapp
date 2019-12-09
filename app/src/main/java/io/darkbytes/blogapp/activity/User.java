package io.darkbytes.blogapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.adapter.PostAdapter;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.entity.response.UserResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.service.handler.UserHandler;
import io.darkbytes.blogapp.utit.DateUtil;
import io.darkbytes.blogapp.utit.PreferenceUtil;

public class User extends AppCompatActivity {

    private PostHandler postHandler = new PostHandler();
    private UserHandler userHandler = new UserHandler();
    private String bearer;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private List<PostResponse> postResponses = new ArrayList<>();
    private PostAdapter postAdapter;

    private TextView name;
    private TextView email;
    private TextView posts;
    private TextView createdAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        bearer = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.BEARER);

        createdAt = findViewById(R.id.user_created_at_text);
        name = findViewById(R.id.user_name_text);
        email = findViewById(R.id.user_email_text);
        posts = findViewById(R.id.user_posts_text);

        shimmerFrameLayout = findViewById(R.id.user_shimmer);

        postAdapter = new PostAdapter(this.postResponses, this);

        recyclerView = findViewById(R.id.user_post_recycler);
        recyclerView.setAdapter(postAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.shimmerFrameLayout.stopShimmer();
    }

    private void assignUser() {

        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        Integer userId = (Integer) bundle.get(Constant.USER_ID);

        if (userId == null)
            return;

        userHandler.getUser(userId, bearer, new Handler<UserResponse>() {
            @Override
            public void success(UserResponse user) {

                createdAt.setText(DateUtil.format(user.getCreateAt()));
                name.setText(user.getName());
                email.setText(user.getEmail());
                posts.setText(user.getPosts().toString());

                loadPost(userId);
            }

            @Override
            public void failure(ErrorResponse errorBody) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Snackbar.make(recyclerView, errorBody.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadPost(Integer userId) {
        postHandler.getPostsByUser(recyclerView, userId, bearer, new Handler<List<PostResponse>>() {
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
