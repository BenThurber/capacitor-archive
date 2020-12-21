package com.example.demo.controller;

import com.example.demo.payload.request.CaptchaVerifyRequest;
import com.example.demo.payload.request.GoogleCaptchaAPIRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/captcha")
public class CaptchaController {


    public CaptchaController() {
    }


//    /**
//     * Get all users from the database and return List of UserResponse objects
//     * @return UserResponse objects
//     */
//    @GetMapping(value = "/all")
//    public List<UserResponse> getAllUsers() {
//        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
//    }


    @PostMapping(value = "verify")
    public boolean verifyCaptcha(@Validated @RequestBody CaptchaVerifyRequest captchaVerifyRequest, HttpServletResponse response) {
        String token = captchaVerifyRequest.getCaptchaResponseToken();
        System.out.println("Captcha token:");
        System.out.println(token);

        GoogleCaptchaAPIRequest googleRequest = new GoogleCaptchaAPIRequest("6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe", token);
        // Send the request here somehow

        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }

}
