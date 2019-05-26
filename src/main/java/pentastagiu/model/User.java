package pentastagiu.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class holds information for an user:
 * username, password and the list of accounts owned.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @OneToOne(mappedBy = "user",cascade = {CascadeType.ALL})
    private Person details;

    @OneToOne(mappedBy = "user",cascade = {CascadeType.ALL})
    private Authentication reference;

    /**
     * The list of notifications for user
     */
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.ALL})
    private List<Notification> notificationList = new ArrayList<>();

    /**
     * The list of all accounts owned by the user
     */
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.ALL})
    private List<Account> accountsList = new ArrayList<>();

    public User(){

    }

    @PrePersist
    private void settingDate(){
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }
    /**
     * Constructs an user with the information received
     * @param username of the user created
     * @param password of the user created
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        settingDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<Account> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(List<Account> accountsList) {
        this.accountsList = accountsList;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Person getDetails() {
        return details;
    }

    public void setDetails(Person details) {
        this.details = details;
    }

    public Authentication getReference() {
        return reference;
    }

    public void setReference(Authentication reference) {
        this.reference = reference;
    }

    /**
     * This method overrites the equals method used to compare 2 users.
     * @param o the user that we compare with current instance
     * @return true if the users are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }


}
