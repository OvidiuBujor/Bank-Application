package pentastagiu.convertor;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String username;
    private String details;
    private LocalDateTime createdTime;

    public NotificationDTO(String username, String details, LocalDateTime createdTime){
        this.username = username;
        this.details = details;
        this.createdTime = createdTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
