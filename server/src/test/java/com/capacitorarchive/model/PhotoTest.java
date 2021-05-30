package com.capacitorarchive.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    @Test
    void getSmallestThumbnailTest() {
        Thumbnail thumbnail1 = new Thumbnail();
        thumbnail1.setSize(10);

        Thumbnail thumbnail2 = new Thumbnail();
        thumbnail2.setSize(20);

        Thumbnail thumbnail3 = new Thumbnail();
        thumbnail3.setSize(30);

        Photo photo = new Photo();
        photo.setThumbnails(new HashSet<>(Arrays.asList(thumbnail1, thumbnail2, thumbnail3)));

        assertEquals(thumbnail1, photo.getSmallestThumbnail());
    }
}
