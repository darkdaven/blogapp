package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.adapter.PostCommentAdapter;
import io.darkbytes.blogapp.entity.event.LikePostEvent;
import io.darkbytes.blogapp.entity.event.NewCommentEvent;
import io.darkbytes.blogapp.entity.event.NewPostEvent;
import io.darkbytes.blogapp.entity.event.UserConnectedEvent;
import io.darkbytes.blogapp.entity.event.UserDisconnectEvent;
import io.darkbytes.blogapp.entity.event.ViewPostEvent;
import io.darkbytes.blogapp.entity.request.PostCommentRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostCommentResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.service.websocket.EventListener;
import io.darkbytes.blogapp.service.websocket.WebSocketService;
import io.darkbytes.blogapp.utit.ChipUtil;
import io.darkbytes.blogapp.utit.DateUtil;
import io.darkbytes.blogapp.utit.PreferenceUtil;

public class ViewPost extends AppCompatActivity {

    private PostHandler postHandler = new PostHandler();
    private PostResponse workingPost = null;
    private String bearer;
    private String token;
    private Integer userId;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private PostCommentAdapter postCommentAdapter;
    private List<PostCommentResponse> postComments = new ArrayList<>();

    private TextView createdAt;
    private TextView name;
    private TextView email;
    private TextView title;
    private TextView body;
    private TextView likes;
    private TextView comments;
    private TextView views;
    private ChipGroup tags;
    private EditText commentBox;
    private Button saveComment;
    private ImageButton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        bearer = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.BEARER);
        token = PreferenceUtil.getString(getApplicationContext(), CredentialConstant.TOKEN);
        userId = PreferenceUtil.getInt(getApplicationContext(), CredentialConstant.USER_ID);

        shimmerFrameLayout = findViewById(R.id.view_post_shimmer_view);

        createdAt = findViewById(R.id.view_post_createdAt_text);
        name = findViewById(R.id.view_post_name_text);
        email = findViewById(R.id.view_post_email_text);
        title = findViewById(R.id.view_post_title_text);
        body = findViewById(R.id.view_post_body_text);
        likes = findViewById(R.id.view_post_likes_text);
        comments = findViewById(R.id.view_post_comments_text);
        views = findViewById(R.id.view_post_views_text);
        tags = findViewById(R.id.view_post_tags_chips_group);
        commentBox = findViewById(R.id.view_post_comment_edit);
        saveComment = findViewById(R.id.view_post_save_comment_button);
        profile = findViewById(R.id.view_post_profile_image);

        postCommentAdapter = new PostCommentAdapter(this.postComments, this);

        recyclerView = findViewById(R.id.post_view_recycler_view);
        recyclerView.setAdapter(postCommentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        handleWebSocketEvent();

        assignPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignPost();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.shimmerFrameLayout.stopShimmer();
    }

    private void assignPost() {

        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        PostResponse passPost = (PostResponse) bundle.get(Constant.POST);

        if (passPost == null)
            return;

        postHandler.getPost(passPost.getId(), bearer, new Handler<PostResponse>() {
            @Override
            public void success(PostResponse post) {
                workingPost = post;

                createdAt.setText(DateUtil.format(post.getCreatedAt()));
                name.setText(post.getUserName());
                email.setText(post.getUserEmail());
                title.setText(post.getTitle());
                body.setText(post.getBody());
                likes.setText(post.getLikes().toString());
                comments.setText(post.getComments().toString());
                views.setText(post.getViews().toString());
                ChipUtil.createChips(post.getTags(), getApplicationContext(), tags);

                int favoriteRecourse = (post.getLiked())
                        ? R.drawable.ic_favorite_24px
                        : R.drawable.ic_unfavorite_24px;
                likes.setCompoundDrawablesWithIntrinsicBounds(0, 0, favoriteRecourse, 0);

                profile.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), User.class);
                    intent.putExtra(Constant.USER_ID, post.getUserId());

                    startActivity(intent);
                });

                getComments(post, recyclerView, bearer);
            }

            @Override
            public void failure(ErrorResponse errorBody) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Snackbar.make(recyclerView, errorBody.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        saveComment(passPost);


        likes.setOnClickListener(v -> postHandler.addOrRemoveLike(workingPost, bearer, new Handler<PostResponse>() {
            @Override
            public void success(PostResponse response) {
                workingPost.setLiked(response.getLiked());
                workingPost.setLikes(response.getLikes());
                updateUiPost(response);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                Snackbar.make(v, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }));
    }

    private void getComments(PostResponse post, View view, String bearer) {
        postHandler.getComments(post.getId(), view, bearer, new Handler<List<PostCommentResponse>>() {
            @Override
            public void success(List<PostCommentResponse> response) {
                postComments.clear();
                postComments.addAll(response);
                postCommentAdapter.notifyDataSetChanged();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void failure(ErrorResponse errorBody) {
                Snackbar.make(view, errorBody.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void saveComment(PostResponse post) {

        saveComment.setOnClickListener(v -> {

            if (commentBox.getText() == null || commentBox.getText().toString().isEmpty()) {
                commentBox.setError("Please fill the comment");
                commentBox.requestFocus();
                return;
            }

            saveComment.setText("Saving...");
            saveComment.setEnabled(false);

            PostCommentRequest request = new PostCommentRequest(commentBox.getText().toString());
            postHandler.savePostComment(post.getId(), v, request, bearer, new Handler<PostCommentResponse>() {
                @Override
                public void success(PostCommentResponse response) {
                    Snackbar.make(v, "Comment saved successfully", Snackbar.LENGTH_LONG);
                    postComments.add(response);
                    postCommentAdapter.notifyItemChanged(postComments.size() - 1);
                    clearComment();
                }

                @Override
                public void failure(ErrorResponse errorBody) {
                    Snackbar.make(v, errorBody.getMessage(), Snackbar.LENGTH_LONG);
                    saveComment.setEnabled(true);
                    saveComment.setText("Comment");
                }
            });
        });
    }

    private void clearComment() {
        saveComment.setText("Comment");
        saveComment.setEnabled(true);
        commentBox.setText("");
    }

    private void handleWebSocketEvent() {
        WebSocketService.getInstance(token).subscribe(new EventListener() {
            public void onLikePostEvent(LikePostEvent event) {

                runOnUiThread(() -> {
                    if (workingPost == null || workingPost.getId().intValue() != event.getPostId().intValue())
                        return;

                    workingPost.setLikes(event.getLikes());
                    updateUiPost(workingPost);
                });
            }

            @Override
            public void onViewPostEvent(ViewPostEvent event) {
                runOnUiThread(() -> {

                    if (workingPost == null || workingPost.getId().intValue() != event.getPostId().intValue())
                        return;

                    workingPost.setViews(event.getViews());
                    updateUiPost(workingPost);
                });
            }

            @Override
            public void onNewCommentEvent(NewCommentEvent event) {
                runOnUiThread(() -> {

                    if (workingPost == null)
                        return;

                    if (workingPost.getId().intValue() == event.getPostId().intValue()) {

                        workingPost.setComments(event.getComments());
                        updateUiPost(workingPost);

                        if (event.getUserId() != userId) {
                            PostCommentResponse comment = PostCommentResponse.fromNewCommentEvent(event);
                            postComments.add(comment);
                            int index = postComments.size() - 1;
                            postCommentAdapter.notifyItemInserted(index);

                            Snackbar.make(recyclerView, String.format("%s add a comment", event.getUserName()), Snackbar.LENGTH_LONG)
                                    .show();
                        }

                    } else {
                        Snackbar.make(recyclerView, String.format("%s add a comment", event.getUserName()), Snackbar.LENGTH_LONG)
                                .setAction("View Comment", v -> {
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
                    Snackbar.make(recyclerView, String.format("%s create new Post", event.getUserName()), Snackbar.LENGTH_LONG)
                            .setAction("View Post", v -> {
                                Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                intent.putExtra(Constant.POST, new PostResponse(event.getPost().getId()));
                                startActivity(intent);
                            }).show();
                });
            }

            @Override
            public void onUserConnected(UserConnectedEvent event) {
                Snackbar.make(recyclerView,
                        String.format("%s is now connected", event.getUserEmail()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                Snackbar.make(recyclerView,
                        String.format("%s disconnect", event.getUserEmail()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void updateUiPost(PostResponse post) {
        int favoriteRecourse = (post.getLiked())
                ? R.drawable.ic_favorite_24px
                : R.drawable.ic_unfavorite_24px;

        likes.setCompoundDrawablesWithIntrinsicBounds(0, 0, favoriteRecourse, 0);
        likes.setText(post.getLikes().toString());
        comments.setText(post.getComments().toString());
        views.setText(post.getViews().toString());
    }
}
