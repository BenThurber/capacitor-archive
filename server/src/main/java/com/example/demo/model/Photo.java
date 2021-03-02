package com.example.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
public class Photo extends FileReference {

    @Column(name = "order")
    private Integer order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capacitor_unit_id", nullable = false)
    private CapacitorUnit capacitorUnit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "photo", cascade = CascadeType.ALL)
    private List<Thumbnail> thumbnails = new ArrayList<>();

    public Photo() {}

}
