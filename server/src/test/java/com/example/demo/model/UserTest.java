package com.example.demo.model;

import com.example.demo.payload.request.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    private User user1;

    @BeforeEach
    private void setup() {
        UserRegisterRequest rr = new UserRegisterRequest();
        rr.setFirstName("John");
        rr.setLastName("Smith");
        rr.setEmail("example@example.com");
        rr.setEmailVisible(true);
        rr.setPassword("password1");
        rr.setUsername("JSmith");

        user1 = new User(rr);
    }

    @Test
    void checkPassword() {
        assertTrue(user1.checkPassword("password1"));
    }

    @Test
    void setPasswordHash() {
        String newPassword = "abcd123";
        user1.setPassword(newPassword);
        assertNotEquals(ReflectionTestUtils.getField(user1, "passwordHash"), newPassword);
    }
}
