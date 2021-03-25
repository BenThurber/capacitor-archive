package com.example.demo.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PhotoRequest extends FileReferenceRequest {

    @JsonProperty("order")
    private Integer order;

    @NotNull(message = "Photo Request thumbnails can not be null, must at least be an empty array")
    @JsonProperty("thumbnails")
    private Set<ThumbnailRequest> thumbnails = new HashSet<>();

    public PhotoRequest() {
        super();
    }
}
