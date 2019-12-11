package io.darkbytes.blogapp.service.websocket;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Optional;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.activity.ViewPost;
import io.darkbytes.blogapp.adapter.PostAdapter;
import io.darkbytes.blogapp.entity.event.LikePostEvent;
import io.darkbytes.blogapp.entity.event.NewCommentEvent;
import io.darkbytes.blogapp.entity.event.NewPostEvent;
import io.darkbytes.blogapp.entity.event.UserConnectedEvent;
import io.darkbytes.blogapp.entity.event.UserDisconnectEvent;
import io.darkbytes.blogapp.entity.event.ViewPostEvent;
import io.darkbytes.blogapp.entity.response.PostResponse;

public class HomeEventListener extends EventListener {

    private Activity activity;
    private View view;
    private PostAdapter postAdapter;
    private List<PostResponse> postResponses;

    public HomeEventListener(Activity activity, View view, PostAdapter postAdapter,
                             List<PostResponse> postResponses) {
        this.activity = activity;
        this.view = view;
        this.postAdapter = postAdapter;
        this.postResponses = postResponses;
    }

    public void onLikePostEvent(LikePostEvent event) {

        activity.runOnUiThread(() -> {
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
        activity.runOnUiThread(() -> {
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
        activity.runOnUiThread(() -> {
            Optional<PostResponse> existPost = postResponses
                    .stream()
                    .filter(p -> p.getId().intValue() == event.getPostId().intValue())
                    .findFirst();

            if (existPost.isPresent()) {
                PostResponse post = existPost.get();
                post.setComments(event.getComments());
                updateUiPost(post);


                Snackbar.make(view, String.format("%s add a comment", event.getUserName()), Snackbar.LENGTH_LONG)
                        .setAction("View Post", v -> {
                            Intent intent = new Intent(activity.getApplicationContext(), ViewPost.class);
                            intent.putExtra(Constant.POST, new PostResponse(event.getPostId()));
                            activity.startActivity(intent);
                        }).show();
            }
        });
    }

    @Override
    public void onNewPostEvent(NewPostEvent event) {
        activity.runOnUiThread(() -> {
            PostResponse post = PostResponse.fromNewPostEvent(event);
            postResponses.add(post);
            postAdapter.notifyItemInserted(postResponses.size() - 1);

            Snackbar.make(view, String.format("%s create new Post", event.getUserName()), Snackbar.LENGTH_LONG)
                    .setAction("View Post", v -> {
                        Intent intent = new Intent(activity.getApplicationContext(), ViewPost.class);
                        intent.putExtra(Constant.POST, new PostResponse(event.getPost().getId()));
                        activity.startActivity(intent);
                    }).show();
        });
    }

    @Override
    public void onUserConnected(UserConnectedEvent event) {
        Snackbar.make(view,
                String.format("%s is now connected", event.getUserEmail()),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        Snackbar.make(view,
                String.format("%s disconnect", event.getUserEmail()),
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void updateUiPost(PostResponse post) {
        int index = postResponses.indexOf(post);
        postAdapter.notifyItemChanged(index);
    }
}
