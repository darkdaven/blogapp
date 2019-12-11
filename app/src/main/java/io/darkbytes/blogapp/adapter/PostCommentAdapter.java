package io.darkbytes.blogapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.activity.User;
import io.darkbytes.blogapp.entity.response.PostCommentResponse;
import io.darkbytes.blogapp.util.DateUtil;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {

    private List<PostCommentResponse> comments;
    private Context context;

    public PostCommentAdapter(List<PostCommentResponse> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View CommentView = inflater.inflate(R.layout.post_view_comment, parent, false);

        return new PostCommentAdapter.ViewHolder(CommentView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentAdapter.ViewHolder viewHolder, int position) {
        final PostCommentResponse comment = this.comments.get(position);

        viewHolder.createdAt.setText(DateUtil.format(comment.getCreatedAt()));
        viewHolder.name.setText(comment.getUserName());
        viewHolder.email.setText(comment.getUserEmail());
        viewHolder.body.setText(comment.getBody());

        viewHolder.profile.setOnClickListener(v -> {
            Intent intent = new Intent(context, User.class);
            intent.putExtra(Constant.USER_ID, comment.getUserId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton profile;
        public TextView createdAt;
        public TextView name;
        public TextView email;
        public TextView body;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.view_comment_profile_image);
            createdAt = itemView.findViewById(R.id.view_comment_createdAt_text);
            name = itemView.findViewById(R.id.view_comment_name_text);
            email = itemView.findViewById(R.id.view_comment_email_text);
            body = itemView.findViewById(R.id.view_comment_body_text);
        }
    }


}
