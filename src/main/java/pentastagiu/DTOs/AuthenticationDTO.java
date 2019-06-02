package pentastagiu.DTOs;

import java.time.LocalDateTime;

public class AuthenticationDTO {

    private String username;
    private String token;
    private LocalDateTime creationTime;

    public AuthenticationDTO(String username, String token, LocalDateTime creationTime){
        this.username = username;
        this.token = token;
        this.creationTime = creationTime;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
