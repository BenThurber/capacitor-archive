package com.example.demo.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;


/**
 * A response object in the format that is received from https://www.google.com/recaptcha/api/siteverify
 * This response is received for forwarded on to the front end.
 */
public class GoogleCaptchaAPIResponse {


    @JsonProperty(value = "success", required = true)
    private Boolean success;

    /**
     * Timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(value = "challenge_ts")
    private Date challengeTimeStamp;

    /**
     * The hostname of the site where the reCAPTCHA was solved
     */
    @JsonProperty(value = "hostname")
    private String hostname;


    /**
     * The user's IP address.
     */
    @JsonProperty(value = "error-codes")
    private List<String> errorCodes;




    public GoogleCaptchaAPIResponse() {}


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getChallengeTimeStamp() {
        return challengeTimeStamp;
    }

    public void setChallengeTimeStamp(Date challengeTimeStamp) {
        this.challengeTimeStamp = challengeTimeStamp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
