package com.example.demo.model;

import com.example.demo.payload.request.ThumbnailRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"photo"})
@Entity
public class Thumbnail extends FileReference {

    @Column(name = "size", nullable = false)
    private Integer size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    public Thumbnail(ThumbnailRequest thumbnailRequest) {
        super(thumbnailRequest);
        setSize(thumbnailRequest.getSize());
    }

    public Thumbnail() {}

}
