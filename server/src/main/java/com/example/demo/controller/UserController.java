package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.request.UserRegisterRequest;
import com.example.demo.payload.response.UserResponse;
import com.example.demo.repository.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Get all users from the database and return List of UserResponse objects
     * @return UserResponse objects
     */
    @GetMapping(value = "/all")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    /**
     * Register a user
     * @param userRegisterRequest the user to register
     */
    @PostMapping(value = "register")
    public void registerUser(@Validated @RequestBody UserRegisterRequest userRegisterRequest, HttpServletResponse response) {
        userRepository.save(new User(userRegisterRequest));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
