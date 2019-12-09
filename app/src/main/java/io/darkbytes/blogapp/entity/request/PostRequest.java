package io.darkbytes.blogapp.entity.request;

import java.util.List;

public class PostRequest {
    private String title;
    private String body;
    private List<String> tags;

    public PostRequest(String title, String body, List<String> tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
