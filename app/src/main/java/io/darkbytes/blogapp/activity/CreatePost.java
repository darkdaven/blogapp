package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.entity.request.PostRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.PostResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.PostHandler;
import io.darkbytes.blogapp.util.PreferenceUtil;

public class CreatePost extends AppCompatActivity {
    private PostHandler postHandler = new PostHandler();
    private EditText title;
    private EditText body;
    private NachoTextView tags;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        title = findViewById(R.id.create_post_title_text);
        body = findViewById(R.id.create_post_body_text);
        tags = findViewById(R.id.create_post_tags_edit);
        tags.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);

        create = findViewById(R.id.create_post_save_button);
        Button cancel = findViewById(R.id.create_post_cancel_button);

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Home.class);
            startActivity(intent);
        });

        create.setOnClickListener(v -> {
            if (title.getText() == null || title.getText().toString().isEmpty()) {
                title.setError("Please fill the title");
                return;
            }

            if (body.getText() == null || body.getText().toString().isEmpty()) {
                body.setError("Please fill the body");
                return;
            }

            List<String> tagsList = new ArrayList<>();

            if (tags.getText() != null && !tags.getText().toString().isEmpty()) {
                tagsList = tags.getChipValues();
            }

            create.setText("Saving Post...");
            create.setEnabled(false);

            PostRequest request = new PostRequest(title.getText().toString(), body.getText().toString(), tagsList);
            postHandler.savePost(v, request, PreferenceUtil.getString(v.getContext(), CredentialConstant.BEARER), new Handler<PostResponse>() {
                @Override
                public void success(PostResponse response) {

                    Snackbar.make(v, "Post created successfully!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("View Post", viewPost -> {
                                Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                intent.putExtra(Constant.POST, response);
                                startActivity(intent);
                            })
                            .show();

                    clearEdits();
                }

                @Override
                public void failure(ErrorResponse errorBody) {
                    create.setEnabled(true);
                    create.setText("Save Post");
                }
            });
        });
    }

    private void clearEdits() {
        title.setText("");
        body.setText("");
        tags.setText("");
        title.requestFocus();
        create.setEnabled(true);
        create.setText("Save Post");
    }

}
