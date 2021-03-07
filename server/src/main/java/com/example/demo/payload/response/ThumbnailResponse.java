package com.example.demo.payload.response;

import com.example.demo.model.Thumbnail;
import com.example.demo.payload.request.PhotoRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ThumbnailResponse extends FileReferenceResponse {

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("photo")
    private PhotoRequest photo;

    public ThumbnailResponse(Thumbnail thumbnail) {
        super(thumbnail);
        setSize(thumbnail.getSize());
    }

    public ThumbnailResponse() {
    }
}
