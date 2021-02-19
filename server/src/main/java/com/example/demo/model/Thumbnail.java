package com.example.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
public class Thumbnail extends File {

    @Column(name = "size")
    Integer size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    public Thumbnail() {}

}
