package com.example.demo.controller;

import com.example.demo.payload.request.CaptchaVerifyRequest;
import com.example.demo.payload.request.GoogleCaptchaAPIRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping(value = "/captcha")
public class CaptchaController {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CaptchaController() {
    }


    @PostMapping(value = "verify")
    public boolean verifyCaptcha(@Validated @RequestBody CaptchaVerifyRequest captchaVerifyRequest, HttpServletResponse response) throws JsonProcessingException {
        String token = captchaVerifyRequest.getCaptchaResponseToken();
        System.out.println("Captcha token:");
        System.out.println(token + '\n');

        GoogleCaptchaAPIRequest googleRequestBody = new GoogleCaptchaAPIRequest("6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe", token);
        String googleRequestBodyJson = this.objectMapper.writeValueAsString(googleRequestBody);
        System.out.println(googleRequestBodyJson + '\n');



        HttpRequest googleAPIRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com/recaptcha/api/siteverify"))
                .header("Accept", "multipart/form-data")
                .header("Content-Type", "multipart/form-data")
                .POST(HttpRequest.BodyPublishers.ofString(googleRequestBodyJson))
                .build();


        String googleResponseBodyJson;
        try {
            googleResponseBodyJson =  this.httpClient.send(googleAPIRequest, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Google reCAPTCHA API responded with the error: " + e.getMessage());
        }

        System.out.println(googleResponseBodyJson);

        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }

}
