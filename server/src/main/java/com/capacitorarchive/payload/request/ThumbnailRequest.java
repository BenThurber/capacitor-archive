package com.capacitorarchive.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

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
