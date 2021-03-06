package com.capacitorarchive.model;

import com.capacitorarchive.payload.request.PhotoRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"thumbnails", "capacitorUnit", "order"})
@Entity
public class Photo extends FileReference {

    @Column(name = "order")
    private Integer order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capacitor_unit_id", nullable = false)
    private CapacitorUnit capacitorUnit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Thumbnail> thumbnails = new HashSet<>();


    public Photo(PhotoRequest photoRequest) {
        super(photoRequest);
        setOrder(photoRequest.getOrder());
        setThumbnails(photoRequest.getThumbnails().stream().map(Thumbnail::new).collect(Collectors.toSet()));
        thumbnails.forEach(thumbnail -> thumbnail.setPhoto(this));
    }

    public Photo() {}


    @Override
    public String toString() {
        return super.toString() + " Ord: " + order;
    }

}
