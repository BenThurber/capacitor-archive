package com.example.demo.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.google.recaptcha")
public class GoogleReCaptchaAPIKey {


    /**
     * This is the private key used to make API calls to https://www.google.com/recaptcha/api/siteverify
     * The default value here is the test key and is safe to commit
     */
    private String key = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";

    public GoogleReCaptchaAPIKey() { }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
