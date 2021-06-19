package com.capacitorarchive.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementTest {

    @Test
    void oneMM() {
        Measurement measurement = new Measurement("1", Measurement.Unit.MM);
        assertEquals(1000, measurement.getMeasurementUm());
    }

    @Test
    void oneCM() {
        Measurement measurement = new Measurement("1", Measurement.Unit.CM);
        assertEquals(10000, measurement.getMeasurementUm());
    }

    @Test
    void oneIN() {
        Measurement measurement = new Measurement("1", Measurement.Unit.IN);
        assertEquals(25400, measurement.getMeasurementUm());
    }

    @Test
    void intMM() {
        Measurement measurement = new Measurement("12345", Measurement.Unit.MM);
        assertEquals(12345000, measurement.getMeasurementUm());
    }

    @Test
    void floatMM() {
        Measurement measurement = new Measurement("3.145", Measurement.Unit.MM);
        assertEquals(3145, measurement.getMeasurementUm());
    }

    @Test
    void floatIN() {
        Measurement measurement = new Measurement("3.145", Measurement.Unit.IN);
        assertEquals(79883, measurement.getMeasurementUm());
    }

    @Test
    void fractionMM() {
        Measurement measurement = new Measurement("3/4", Measurement.Unit.MM);
        assertEquals(750, measurement.getMeasurementUm());
    }

    @Test
    void repeatingFractionCM() {
        Measurement measurement = new Measurement("1/3", Measurement.Unit.CM);
        assertEquals(3333, measurement.getMeasurementUm());
    }

}
