package com.capacitorarchive.payload.response;

import com.capacitorarchive.model.FileReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FileReferenceResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("url")
    private String url;

    public FileReferenceResponse(FileReference fileReference) {
        FileReference fr = fileReference;
        setId(fr.getId());
        setUrl(fr.getUrl());
    }

    public FileReferenceResponse() {
    }
}
