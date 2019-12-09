package io.darkbytes.blogapp.entity.event;

public class ViewPostEvent extends Event {
    private Integer postId;
    private Integer views;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "ViewPostEvent{" +
                "postId=" + postId +
                ", views=" + views +
                '}';
    }
}
