package io.darkbytes.blogapp.entity.event;

import com.google.gson.annotations.SerializedName;

public enum EventType {
    @SerializedName("likes")
    LIKES("likes"),

    @SerializedName("disconnected")
    DISCONNECTED("disconnected"),

    @SerializedName("user-connected")
    USER_CONNECTED("user-connected"),

    @SerializedName("view-post")
    VIEW_POST("view-post"),

    @SerializedName("new-comment")
    NEW_COMMENT("new-comment"),

    @SerializedName("logged")
    LOGGED("logged"),

    @SerializedName("new-post")
    NEW_POST("new-post"),

    @SerializedName("error")
    ERROR("error");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}