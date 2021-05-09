package com.capacitorarchive.payload.response;

import com.capacitorarchive.model.Photo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // Convert Thumbnail List to ThumbnailResponse list
        setThumbnails(photo.getThumbnails().stream().map(ThumbnailResponse::new).collect(Collectors.toList()));
    }

    public PhotoResponse() {
    }
}
