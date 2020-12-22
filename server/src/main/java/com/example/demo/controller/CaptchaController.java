package com.example.demo.controller;

import com.example.demo.payload.response.GoogleCaptchaAPIResponse;
import com.example.demo.service.GoogleReCaptchaAPIKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

@RestController
@RequestMapping(value = "/captcha")
public class CaptchaController {


    private final ObjectMapper objectMapper = new ObjectMapper();

    private final GoogleReCaptchaAPIKey keyService;


    public CaptchaController(GoogleReCaptchaAPIKey keyService) {
        this.keyService = keyService;
    }


    @PostMapping(value = "verify")
    public GoogleCaptchaAPIResponse verifyCaptcha(@Validated @RequestBody String captchaClientToken, HttpServletResponse response) throws JsonProcessingException {

        // Construct Request Body
        MultiValueMap<String, String> googleRequestBody = new LinkedMultiValueMap<>(2);
        googleRequestBody.add("secret", this.keyService.getKey());
        googleRequestBody.add("response", captchaClientToken);

        // Construct request and send
        WebClient.ResponseSpec googleWebClientResponse =
                WebClient.create()
                .post()
                .uri("https://www.google.com/recaptcha/api/siteverify")
                .body(BodyInserters.fromFormData(googleRequestBody))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve();


        // Wait for async response to return
        String googleResponseJson;
        try {
            googleResponseJson = googleWebClientResponse
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Google reCAPTCHA API responded with the error: " + e.getMessage());
        }
        if (googleResponseJson == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't get or read response from the Google API");
        }

        GoogleCaptchaAPIResponse googleResponse = objectMapper.readValue(googleResponseJson, GoogleCaptchaAPIResponse.class);


        response.setStatus(HttpServletResponse.SC_OK);
        return googleResponse;
    }

}
