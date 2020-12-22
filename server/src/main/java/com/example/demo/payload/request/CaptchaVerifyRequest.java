package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;


public class CaptchaVerifyRequest {

    @JsonProperty("captchaResponseToken")
    private String captchaResponseToken;


    public CaptchaVerifyRequest() {}


    public String getCaptchaResponseToken() {
        return captchaResponseToken;
    }

    public void setCaptchaResponseToken(String captchaResponseToken) {
        this.captchaResponseToken = captchaResponseToken;
    }
}
