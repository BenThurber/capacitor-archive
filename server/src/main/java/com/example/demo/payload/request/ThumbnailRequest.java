package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ThumbnailRequest extends FileReferenceRequest {

    @Min(value = 0, message = "thumbnail size must be non-negative")
    @JsonProperty("size")
    private Integer size;

    @JsonProperty("photo")
    private PhotoRequest photo;

    public ThumbnailRequest() {
        super();
    }
}
