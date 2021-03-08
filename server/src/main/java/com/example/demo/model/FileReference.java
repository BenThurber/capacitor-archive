package com.example.demo.model;

import com.example.demo.payload.request.FileReferenceRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FileReference {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.TABLE)   // Uses TABLE instead of IDENTITY
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", length = 510, nullable = false)
    private String url;

    FileReference(FileReferenceRequest fileReferenceRequest) {
        this.id = fileReferenceRequest.getId();
        setUrl(fileReferenceRequest.getUrl());
    }

    FileReference() {}
}
