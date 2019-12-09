package io.darkbytes.blogapp.entity.response;

public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer posts;
    private Long createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Long getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(Long createAt) {
        this.createdAt = createAt;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", posts=" + posts +
                ", createdAt=" + createdAt +
                '}';
    }
}
