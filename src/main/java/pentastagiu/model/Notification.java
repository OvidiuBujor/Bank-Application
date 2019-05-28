package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "details")
    private String details;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonIgnoreProperties("notificationList")
    private User user;

    @PrePersist
    void prePersist(){
        this.createdTime = LocalDateTime.now();
        System.out.println("Notification created.");
    }

    @PreDestroy
    void preDestroid(){
        this.sentTime = LocalDateTime.now();
        System.out.println("Notification sent.");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void initializeTimes(){
        this.createdTime =  LocalDateTime.now();
        this.sentTime = LocalDateTime.now();
    }
}
