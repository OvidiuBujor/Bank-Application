package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class hols the authentication details
 * for all the logged in users.
 */
@Entity
@Table(name = "authentication")
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    /**
     * The generated token for the user
     */
    @Column(name = "token", unique = true)
    private String token;
    /**
     * The user that is logged in
     */
    @OneToOne
    @JoinColumn(name= "userID")
    @JsonIgnoreProperties("reference")
    private User user;
    /**
     * Time when the user logged in
     */
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    /**
     * This method adds the creation time of the authentication
     */
    @PrePersist
    private void prePersist(){
        creationTime = LocalDateTime.now();
        System.out.println("Token generated for user '" + user.getUsername() + "'.");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
