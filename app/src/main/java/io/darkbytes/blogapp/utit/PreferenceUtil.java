package io.darkbytes.blogapp.utit;

import android.content.Context;
import android.content.SharedPreferences;

import io.darkbytes.blogapp.CredentialConstant;

public class PreferenceUtil {

    public static String getString(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(CredentialConstant.TOKEN, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static Integer getInt(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(CredentialConstant.TOKEN, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }
}
