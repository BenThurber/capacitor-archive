package com.example.demo.model;

import com.example.demo.payload.request.UserRegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
public class User {

    private final static int FIELD_LEN = 45;
    private final static int BIO_LEN = 500;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", length = FIELD_LEN)
    private String firstName;

    @Column(name = "last_name", length = FIELD_LEN)
    private String lastName;

    @Column(name = "email", length = FIELD_LEN, nullable = false)
    private String email;

    @Column(name = "password_hash", columnDefinition="CHAR(60)", nullable = false)
    private String passwordHash;

    @Column(name = "email_visible", nullable = false)
    private Boolean emailVisible;

    @Column(name = "username", length = FIELD_LEN, unique = true)
    private String username;

    @Column(name = "bio", length = BIO_LEN)
    private String bio;


    /**Create a User from a UserRegisterRequest*/
    public User(UserRegisterRequest userRegisterRequest) {
        UserRegisterRequest r = userRegisterRequest;

        setFirstName(r.getFirstName());
        setLastName(r.getLastName());
        setEmail(r.getEmail());
        setPassword(r.getPassword());
        setEmailVisible(r.getEmailVisible());
        setUsername(r.getUsername());
        setBio(r.getBio());
    }

    private User() {}


    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Hashes password and compares it to the password hash of the user
     * @param password plaintext password
     * @return true if the given password is correct
     */
    public boolean checkPassword(String password) {
        return encoder.matches(password, this.passwordHash);
    }

    /**
     * Converts plaintext password to a hash with BCryptPasswordEncoder
     * @param password a plaintext password
     */
    public void setPassword(String password) {
        this.passwordHash = password != null ? encoder.encode(password) : null;
    }

    public Boolean getEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(Boolean emailVisible) {
        this.emailVisible = emailVisible;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
