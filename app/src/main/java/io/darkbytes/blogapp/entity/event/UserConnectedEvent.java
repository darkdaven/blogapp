package io.darkbytes.blogapp.entity.event;

public class UserConnectedEvent extends Event {
    private String userEmail;
    private Integer userId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
