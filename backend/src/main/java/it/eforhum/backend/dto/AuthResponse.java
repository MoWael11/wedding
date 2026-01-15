package it.eforhum.backend.dto;

public class AuthResponse {

    private Integer userId;
    private String token;
    private String username;
    private String role;

    public AuthResponse() {}

    public AuthResponse(Integer userId, String token, String username, String role) {
        this.userId = userId;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
