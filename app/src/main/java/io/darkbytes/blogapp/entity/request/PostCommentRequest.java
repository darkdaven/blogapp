package io.darkbytes.blogapp.entity.request;

public class PostCommentRequest {
    private String body;

    public PostCommentRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
