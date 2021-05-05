package com.capacitorarchive.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@EqualsAndHashCode
public class UserRegisterRequest {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "This user needs an email")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "This user needs a password")
    @JsonProperty("password")
    private String password;

    /**Is true if a user wants their email to be displayed on their profile*/
    @JsonProperty("emailVisible")
    private Boolean emailVisible;

    @JsonProperty("username")
    private String username;

    @JsonProperty("bio")
    private String bio;


    public UserRegisterRequest() {}




}
