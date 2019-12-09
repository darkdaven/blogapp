package io.darkbytes.blogapp.entity.response;

import io.darkbytes.blogapp.entity.event.NewCommentEvent;

public class PostCommentResponse {
    private Integer id;
    private String body;
    private Integer postId;
    private Integer userId;
    private String userName;
    private String userEmail;
    private Long createdAt;

    public static PostCommentResponse fromNewCommentEvent(NewCommentEvent event) {
        PostCommentResponse comment = new PostCommentResponse();
        comment.setId(event.getCommendId());
        comment.setBody(event.getCommentBody());
        comment.setUserId(event.getUserId());
        comment.setPostId(event.getPostId());
        comment.setUserName(event.getUserName());
        comment.setUserEmail(event.getUserEmail());

        return comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "PostCommentResponse{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", postId=" + postId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
