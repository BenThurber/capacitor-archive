package com.example.demo.model;

import com.example.demo.payload.request.UserRegisterRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class User {

    private final static int FIELD_LEN = 45;
    private final static int BIO_LEN = 500;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", length = FIELD_LEN)
    private String firstName;

    @Column(name = "last_name", length = FIELD_LEN)
    private String lastName;

    @Column(name = "email", length = FIELD_LEN, nullable = false)
    private String email;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
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


}
