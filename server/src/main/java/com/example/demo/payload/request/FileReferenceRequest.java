package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FileReferenceRequest {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "File is missing a url")
    @Size(min = 1, message = "File is missing a url")
    @JsonProperty("url")
    private String url;

    public FileReferenceRequest() {}

}
