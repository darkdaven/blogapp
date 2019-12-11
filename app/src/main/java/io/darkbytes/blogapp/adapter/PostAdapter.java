package io.darkbytes.blogapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.activity.User;
import io.darkbytes.blogapp.activity.ViewPost;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.util.ChipUtil;
import io.darkbytes.blogapp.util.DateUtil;
import io.darkbytes.blogapp.util.PreferenceUtil;
import io.darkbytes.blogapp.util.StringUtil;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<PostResponse> postResponses;
    private Context context;
    private PostHandler postHandler = new PostHandler();
    private String bearer;

    public PostAdapter(List<PostResponse> postResponses, Context context) {
        this.postResponses = postResponses;
        this.context = context;
        this.bearer = PreferenceUtil.getString(context, CredentialConstant.BEARER);
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View PostView = inflater.inflate(R.layout.post_item, parent, false);

        return new PostAdapter.ViewHolder(PostView);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        final PostResponse post = this.postResponses.get(position);

        viewHolder.profile.setOnClickListener(v -> {
            Intent intent = new Intent(context, User.class);
            intent.putExtra(Constant.USER_ID, post.getUserId());

            context.startActivity(intent);
        });

        viewHolder.createdAt.setText(DateUtil.format(post.getCreatedAt()));
        viewHolder.name.setText(post.getUserName());
        viewHolder.email.setText(post.getUserEmail());
        viewHolder.title.setText(post.getTitle());
        viewHolder.body.setText(StringUtil.shortBody(post.getBody(), 60));
        viewHolder.likes.setText(post.getLikes().toString());
        viewHolder.comments.setText(post.getComments().toString());
        viewHolder.views.setText(post.getViews().toString());

        updateLikeStatus(post, viewHolder);
        viewHolder.tags.removeAllViews();
        ChipUtil.createChips(post.getTags(), context, viewHolder.tags);

        viewHolder.likes.setOnClickListener(v -> postHandler.addOrRemoveLike(post, bearer, new Handler<PostResponse>() {
            @Override
            public void success(PostResponse response) {
                post.setLiked(response.getLiked());
                updateLikeStatus(response, viewHolder);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                Snackbar.make(v, errorResponse.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }));


        viewHolder.holder.setOnClickListener(v ->

        {
            Intent intent = new Intent(context, ViewPost.class);
            intent.putExtra(Constant.POST, post);
            context.startActivity(intent);
        });
    }

    private void updateLikeStatus(final PostResponse post, ViewHolder viewHolder) {

        int favoriteRecourse = (post.getLiked())
                ? R.drawable.ic_favorite_24px
                : R.drawable.ic_unfavorite_24px;

        viewHolder.likes.setCompoundDrawablesWithIntrinsicBounds(0, 0, favoriteRecourse, 0);
        viewHolder.likes.setText(post.getLikes().toString());
    }


    @Override
    public int getItemCount() {
        return this.postResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton profile;
        public CardView holder;
        public TextView createdAt;
        public TextView name;
        public TextView email;
        public TextView title;
        public TextView body;
        public TextView likes;
        public TextView comments;
        public TextView views;
        public ChipGroup tags;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.post_item_profile_image);
            holder = itemView.findViewById(R.id.post_item_holder_card);
            createdAt = itemView.findViewById(R.id.post_item_createdAt_text);
            name = itemView.findViewById(R.id.post_item_name_text);
            email = itemView.findViewById(R.id.post_item_email_text);
            title = itemView.findViewById(R.id.post_item_title_text);
            body = itemView.findViewById(R.id.post_item_body_text);
            likes = itemView.findViewById(R.id.post_item_likes_text);
            comments = itemView.findViewById(R.id.post_item_comments_text);
            views = itemView.findViewById(R.id.post_item_views_text);
            tags = itemView.findViewById(R.id.post_item_tags_chips_group);
        }
    }
}
