package com.example.demo.payload.response;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
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



}
