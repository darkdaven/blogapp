package io.darkbytes.blogapp.entity.response;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import io.darkbytes.blogapp.entity.event.NewPostEvent;

public class PostResponse implements Serializable {
    private Integer id;
    private String title;
    private String body;
    private Boolean liked;
    private Long createdAt;
    private Integer userId;
    private String userName;
    private String userEmail;
    private Integer comments;
    private Integer views;
    private Integer likes;
    private List<String> tags;


    public static PostResponse fromNewPostEvent(final NewPostEvent event) {
        PostResponse post = new PostResponse();
        post.setId(event.getPost().getId());
        post.setTitle(event.getPost().getTitle());
        post.setBody(event.getPost().getBody());
        post.setLiked(event.getPost().getLiked());
        post.setCreatedAt(event.getPost().getCreatedAt());
        post.setUserId(event.getUserId());
        post.setUserName(event.getUserName());
        post.setUserEmail(event.getUserEmail());
        post.setComments(event.getPost().getComments());
        post.setViews(event.getPost().getViews());
        post.setLikes(event.getPost().getLikes());
        if (event.getPost().getTags() != null && event.getPost().getTags().size() > 0)
            post.setTags(Arrays.asList(event.getPost().getTags().get(0).split(",")));
        
        return post;
    }

    public PostResponse() {
    }

    public PostResponse(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", liked=" + liked +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", comments=" + comments +
                ", views=" + views +
                ", likes=" + likes +
                ", tags=" + tags +
                '}';
    }
}
