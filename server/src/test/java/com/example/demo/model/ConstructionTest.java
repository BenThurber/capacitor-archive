package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructionTest {

    @Test
    void setConstructionName() {
        Construction construction = new Construction();
        construction.setConstructionName("wax-paper");
        assertEquals("Wax-Paper", construction.getConstructionName());
    }

    @Test
    void createConstruction() {
        Construction construction = new Construction("wax-paper");
        assertEquals("Wax-Paper", construction.getConstructionName());
    }
}
