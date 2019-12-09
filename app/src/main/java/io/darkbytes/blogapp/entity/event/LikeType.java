package io.darkbytes.blogapp.entity.event;

import com.google.gson.annotations.SerializedName;

public enum LikeType {
    @SerializedName("like")
    LIKE("like"),

    @SerializedName("dislike")
    DISLIKE("dislike");

    private final String likeType;

    LikeType(String likeType) {
        this.likeType = likeType;
    }

    public String getLikeType() {
        return likeType;
    }
}
