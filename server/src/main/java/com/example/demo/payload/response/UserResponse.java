package com.example.demo.payload.response;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;


public class UserResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    @JsonProperty("email")
    private String email;

    /**Is true if a user wants their email to be displayed on their profile*/
    @JsonProperty("emailVisible")
    private Boolean emailVisible;

    @JsonProperty("username")
    private String username;

    @JsonProperty("bio")
    private String bio;


    public UserResponse(User user) {
        setId(user.getId());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
        setEmailVisible(user.getEmailVisible());
        setUsername(user.getUsername());
        setBio(user.getBio());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
