package com.example.demo.payload.response;

import com.example.demo.model.Photo;
import com.example.demo.model.Thumbnail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PhotoResponse extends FileReferenceResponse {

    @JsonProperty("order")
    private Integer order;

    @JsonProperty("thumbnails")
    private List<ThumbnailResponse> thumbnails = new ArrayList<>();

    @JsonProperty("capacitorUnitValue")
    private String capacitorUnitValue;

    public PhotoResponse(Photo photo) {
        super(photo);
        setOrder(photo.getOrder());
        setCapacitorUnitValue(photo.getCapacitorUnit().getValue());
    }

    public PhotoResponse() {
    }
}
