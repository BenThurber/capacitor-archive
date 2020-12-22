package com.example.demo.controller;

import com.example.demo.payload.request.CaptchaVerifyRequest;
import com.example.demo.payload.request.GoogleCaptchaAPIRequest;
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
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

@RestController
@RequestMapping(value = "/captcha")
public class CaptchaController {


    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
//    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final WebClient webClient = WebClient.create("https://www.google.com");

    public CaptchaController() {
    }


    @PostMapping(value = "verify")
    public boolean verifyCaptcha(@Validated @RequestBody CaptchaVerifyRequest captchaVerifyRequest, HttpServletResponse response) throws JsonProcessingException {
        String token = captchaVerifyRequest.getCaptchaResponseToken();
        System.out.println("Captcha token:");
        System.out.println(token + '\n');

//        GoogleCaptchaAPIRequest googleRequestBody = new GoogleCaptchaAPIRequest("6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe", token);
//        String googleRequestBodyJson = this.objectMapper.writeValueAsString(googleRequestBody);
//        System.out.println(googleRequestBodyJson + '\n');

//        WebClient.UriSpec<WebClient.RequestBodySpec> request = WebClient.create("https://www.google.com")

        // Construct Body
        MultiValueMap<String, String> googleRequestBody = new LinkedMultiValueMap<>(3);
        googleRequestBody.add("secret", "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe");
        googleRequestBody.add("response", token);

        // Create base URL
        WebClient webClient = WebClient.create("https://www.google.com");

        // Construct request and send
        WebClient.ResponseSpec googleResponse = webClient
                .post()
                .uri("/recaptcha/api/siteverify")
                .body(BodyInserters.fromFormData(googleRequestBody))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED)
                .acceptCharset(Charset.forName("UTF-8"))
                .retrieve();


        String googleResponseJson;
        try {
            googleResponseJson = googleResponse
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Google reCAPTCHA API responded with the error: " + e.getMessage());
        }

        System.out.println(googleResponseJson);

//        Mono<Void> result = this.webClient.post()
//                .uri("/recaptcha/api/siteverify")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromObject(googleRequestBody))
//                .retrieve()
//                .bodyToMono(Void.class);

//        HttpRequest googleAPIRequest = HttpRequest.newBuilder()
//                .uri(URI.create("https://www.google.com/recaptcha/api/siteverify"))
//                .header("Accept", "multipart/form-data")
//                .header("Content-Type", "multipart/form-data")
//                .POST(HttpRequest.BodyPublishers.ofString(googleRequestBodyJson))
//                .build();

//        System.out.println(result);

//        String googleResponseBodyJson;
//        try {
//            googleResponseBodyJson =  this.httpClient.send(googleAPIRequest, HttpResponse.BodyHandlers.ofString()).body();
//        } catch (IOException | InterruptedException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Google reCAPTCHA API responded with the error: " + e.getMessage());
//        }
//
//        System.out.println(googleResponseBodyJson);

        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }

}
