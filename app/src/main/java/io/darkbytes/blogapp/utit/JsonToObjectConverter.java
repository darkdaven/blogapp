package io.darkbytes.blogapp.utit;

import com.google.gson.Gson;

public class JsonToObjectConverter<T> {

    public T convertToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();

        return gson.fromJson(json, clazz);
    }
}