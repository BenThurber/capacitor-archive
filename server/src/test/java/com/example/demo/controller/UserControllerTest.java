package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utilities.JsonConverter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<User> userMockTable = new ArrayList<>();

    @BeforeEach
    void mockRepository() {
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> {
            User newUser = i.getArgument(0);
            ReflectionTestUtils.setField(newUser, "id", (DEFAULT_USER_ID + userCount++));
            userMockTable.add(newUser);
            return newUser;
        });
    }

    private static Long userCount = 0L;
    private static final Long DEFAULT_USER_ID = 1L;


    // ----------- Tests -----------

    private final String newUser1Json = JsonConverter.toJson(true,
            "firstName", "Joey",
            "lastName", "Diaz",
            "email", "comicjoe@aol.com",
            "password", "pass",
            "emailVisible", true,
            "username", "joeylikescapacitors",
            "bio", "Comedian turned radio collector"
    );

    /**
     * Test successful creation of new activity.
     */
    @Test
    void newUser() throws Exception {

        MockHttpServletRequestBuilder httpReq = MockMvcRequestBuilders.post("/user/register")
                .content(newUser1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(httpReq)
                .andExpect(status().isCreated());
    }

}

