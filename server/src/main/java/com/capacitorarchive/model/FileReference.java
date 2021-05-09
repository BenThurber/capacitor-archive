package com.capacitorarchive.model;

import com.capacitorarchive.payload.request.FileReferenceRequest;
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

    /**
     * Given a string like "https://www.some-example.com" will return "https://w...ample.com"
     * @param url any string, url or not
     * @param length the maximum length of the final abbreviated string
     * @return an abbreviated string (minimum size 3 "...")
     */
    public static String abbreviateUrl(String url, int length) {
        if (url.length() < length) {
            return url;
        } else {
            int subStrLen = (length - 3) / 2;
            return url.substring(0, subStrLen) + "..." + url.substring(url.length() - Math.min(subStrLen, url.length()));
        }
    }

    @Override
    public String toString() {
        return String.format("%s \"%s\"", id, FileReference.abbreviateUrl(url, 20));
    }
}
