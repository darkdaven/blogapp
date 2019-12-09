package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.entity.request.RegisterRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.RegisterResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.SecurityHandler;

public class Register extends AppCompatActivity {

    private SecurityHandler securityHandler = new SecurityHandler();
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.register_name_edit);
        email = findViewById(R.id.register_email_edit);
        password = findViewById(R.id.register_password_edit);
        confirmPassword = findViewById(R.id.register_confirm_password_edit);

        final Button createAccount = findViewById(R.id.register_create_account_btn);
        final Button haveAccount = findViewById(R.id.register_have_account_btn);

        haveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Login.class);
            startActivity(intent);
        });

        createAccount.setOnClickListener(v -> {

            if (name.getText() == null || name.getText().toString().isEmpty()) {
                name.setError("Please fill the name");
                return;
            }

            if (email.getText() == null || email.getText().toString().isEmpty()) {
                email.setError("Please fill the email");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                email.setError("Please type an valid email");
                return;
            }

            if (password.getText() == null || password.getText().toString().isEmpty()) {
                password.setError("Please fill the password");
                return;
            }

            if (confirmPassword.getText() == null || confirmPassword.getText().toString().isEmpty()) {
                confirmPassword.setError("Please fill the confirm password");
                return;
            }

            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Snackbar.make(v, "Password and confirm password does not match", Snackbar.LENGTH_LONG).show();
                return;
            }

            createAccount.setText("Creating Account...");
            createAccount.setEnabled(false);

            RegisterRequest request = new RegisterRequest(name.getText().toString(), email.getText().toString(), password.getText().toString());
            securityHandler.register(v, request, new Handler<RegisterResponse>() {
                @Override
                public void success(RegisterResponse response) {
                    haveAccount.callOnClick();
                }

                @Override
                public void failure(ErrorResponse errorBody) {
                    createAccount.setEnabled(true);
                    createAccount.setText("Create Account");
                }
            });
        });
    }
}
