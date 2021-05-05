package com.capacitorarchive.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacitorUnitTest {

    @Test
    void toStringTest1() {
        CapacitorUnit c = new CapacitorUnit();
        c.setCapacitance(100000000L);
        c.setVoltage(600);

        assertEquals("100uf 600V", c.toString());
    }

    @Test
    void toStringTestMissingVoltageAndId() {
        CapacitorUnit c = new CapacitorUnit();
        c.setCapacitance(100000000L);
        c.setVoltage(0);

        assertEquals("100uf", c.toString());
    }

    @Test
    void toStringTestMissingVoltage() {
        CapacitorUnit c = new CapacitorUnit();
        c.setCapacitance(100000000L);
        c.setVoltage(0);

        assertEquals("100uf", c.toString());
    }

    @Test
    void formatCapacitanceTestNegative() {
        assertEquals("-1pf", CapacitorUnit.formatCapacitance(-1L));
    }

    @Test
    void formatCapacitanceTestPico() {
        assertEquals("999pf", CapacitorUnit.formatCapacitance(999L));
    }

    @Test
    void formatCapacitanceTestNano() {
        assertEquals("1nf", CapacitorUnit.formatCapacitance(1000L));
    }

    @Test
    void formatCapacitanceTestMicro1() {
        assertEquals("1uf", CapacitorUnit.formatCapacitance(1000000L));
    }

    @Test
    void formatCapacitanceTestMicro2() {
        assertEquals("999999uf", CapacitorUnit.formatCapacitance(999999000000L));
    }

    @Test
    void formatCapacitanceTestFarads() {
        assertEquals("1F", CapacitorUnit.formatCapacitance(1000000000000L));
    }

    @Test
    void formatCapacitanceTestNanoFloating() {
        assertEquals("1.11nf", CapacitorUnit.formatCapacitance(1111L));
    }

    @Test
    void formatCapacitanceTestMicroFloating() {
        assertEquals("2uf", CapacitorUnit.formatCapacitance(1999999L));
    }

    @Test
    void formatCapacitanceTestFaradFloating() {
        assertEquals("19.99F", CapacitorUnit.formatCapacitance(19994999999999L));
    }


    @Test
    void comparisonTest__capacitance() {
        CapacitorUnit c1 = new CapacitorUnit();
        c1.setCapacitance(100000000L);
        c1.setVoltage(600);

        CapacitorUnit c2 = new CapacitorUnit();
        c2.setCapacitance(100000001L);
        c2.setVoltage(600);

        assertTrue(c1.compareTo(c2) < 0);
    }

    @Test
    void comparisonTest__voltage() {
        CapacitorUnit c1 = new CapacitorUnit();
        c1.setCapacitance(100000000L);
        c1.setVoltage(601);

        CapacitorUnit c2 = new CapacitorUnit();
        c2.setCapacitance(100000000L);
        c2.setVoltage(600);

        assertTrue(c1.compareTo(c2) > 0);
    }

    @Test
    void comparisonTest__equal() {
        CapacitorUnit c1 = new CapacitorUnit();
        c1.setCapacitance(100000000L);
        c1.setVoltage(600);

        CapacitorUnit c2 = new CapacitorUnit();
        c2.setCapacitance(100000000L);
        c2.setVoltage(600);

        assertEquals(0, c1.compareTo(c2));
    }
}
