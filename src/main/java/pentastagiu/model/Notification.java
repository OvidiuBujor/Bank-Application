package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pentastagiu.convertor.NotificationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class holds the information for a
 * notification that is created in the
 * moment of a transfer.
 */
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    /**
     * The details of the transfer
     */
    @Column(name = "details")
    private String details;
    /**
     * Time when the transfer was created
     */
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    /**
     * Time when the notification was send to the user
     */
    @Column(name = "sent_time")
    private LocalDateTime sentTime;
    /**
     * Status of the notification:
     * SEND(when the notification was sent to the user) or
     * NOT_SEND(when the notification was not sent yet to the user)
     */
    @Column(name = "status")
    private NotificationStatus status;
    /**
     * The user that initiate the transfer
     */
    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonIgnoreProperties("notificationList")
    private User user;

    /**
     * This methods adds the time when the notification was created
     * and sets the status of the notification to NOT_SEND.
     */
    @PrePersist
    void prePersist(){
        this.createdTime = LocalDateTime.now();
        this.status = NotificationStatus.NOT_SEND;
        System.out.println("Notification created.");
    }

    /**
     * This method adds the time when the notification was send
     * to the user and updates the status of the notification to
     * SEND.
     */
    @PreUpdate
    void preUpdate(){
        this.sentTime = LocalDateTime.now();
        this.status = NotificationStatus.SEND;
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

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
