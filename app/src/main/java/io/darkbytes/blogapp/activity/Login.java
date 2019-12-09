package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import io.darkbytes.blogapp.R;
import io.darkbytes.blogapp.entity.request.LoginRequest;
import io.darkbytes.blogapp.entity.response.ErrorResponse;
import io.darkbytes.blogapp.entity.response.LoginResponse;
import io.darkbytes.blogapp.service.handler.Handler;
import io.darkbytes.blogapp.service.handler.SecurityHandler;

public class Login extends AppCompatActivity {

    private SecurityHandler securityHandler = new SecurityHandler();
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_edit);
        password = findViewById(R.id.password_edit);
        final Button register = findViewById(R.id.register_button);
        final Button loginButton = findViewById(R.id.login_button);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Register.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {

            if (username.getText() == null || username.getText().toString().isEmpty()) {
                username.setError("Please fill the email");
                return;
            }

            if (password.getText() == null || password.getText().toString().isEmpty()) {
                password.setError("Please fill the password");
                return;
            }

            loginButton.setText("Logging...");
            loginButton.setEnabled(false);

            LoginRequest request = new LoginRequest(username.getText().toString(), password.getText().toString());
            securityHandler.login(v, request, new Handler<LoginResponse>() {
                @Override
                public void success(LoginResponse response) {
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void failure(ErrorResponse errorBody) {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                }
            });
        });
    }
}
