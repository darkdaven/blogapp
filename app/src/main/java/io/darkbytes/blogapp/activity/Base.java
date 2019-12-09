package io.darkbytes.blogapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.darkbytes.blogapp.CredentialConstant;
import io.darkbytes.blogapp.utit.PreferenceUtil;

public class Base extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent;
        if (!PreferenceUtil.getString(getApplicationContext(), CredentialConstant.BEARER).isEmpty()) {
            intent = new Intent(getApplicationContext(), Home.class);
        } else {
            intent = new Intent(getApplicationContext(), Login.class);

        }
        startActivity(intent);

        finish();
    }
}
