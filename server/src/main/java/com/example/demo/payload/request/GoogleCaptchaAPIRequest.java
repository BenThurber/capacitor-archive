package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GoogleCaptchaAPIRequest {

    /**
     * The shared key between your site and reCAPTCHA.  The secret key from google.
     */
    @JsonProperty(value = "secret", required = true)
    private String secret;

    /**
     * The user response token provided by the reCAPTCHA client-side integration on your site.  The token from the
     * post request verifyCaptcha.
     */
    @JsonProperty(value = "response", required = true)
    private String response;

    /**
     * The user's IP address.
     */
    @JsonProperty(value = "remoteip")
    private String remoteip;



    public GoogleCaptchaAPIRequest(String secret, String response, String remoteip) {
        this.secret = secret;
        this.response = response;
        this.remoteip = remoteip;
    }

    public GoogleCaptchaAPIRequest(String secret, String response) {
        this.secret = secret;
        this.response = response;
    }

    public GoogleCaptchaAPIRequest() {}



    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRemoteip() {
        return remoteip;
    }

    public void setRemoteip(String remoteip) {
        this.remoteip = remoteip;
    }
}
