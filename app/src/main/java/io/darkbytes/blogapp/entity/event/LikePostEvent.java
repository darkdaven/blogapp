package io.darkbytes.blogapp.entity.event;


public class LikePostEvent extends Event {
    private LikeType likeType;
    private Integer postId;
    private String postTitle;
    private Integer likes;

    public LikeType getLikeType() {
        return likeType;
    }

    public void setLikeType(LikeType likeType) {
        this.likeType = likeType;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "LikePostEvent{" +
                "likeType=" + likeType +
                ", postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", likes=" + likes +
                '}';
    }
}
