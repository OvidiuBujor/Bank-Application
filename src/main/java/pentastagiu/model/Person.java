package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * This class holds the information for
 * an User
 */
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    /**
     * The address of the user
     */
    @Column(name = "address")
    private String address;
    /**
     * First name of the user
     */
    @Column(name = "first_name")
    private String firstName;
    /**
     * Last name of the user
     */
    @Column(name = "last_name")
    private String lastName;
    /**
     * Email of the user
     */
    @Column(name = "email")
    private String email;
    /**
     * The corresponding user, creates the connection
     * with the User class
     */
    @OneToOne
    @JoinColumn(name= "userID")
    @JsonIgnoreProperties("details")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
