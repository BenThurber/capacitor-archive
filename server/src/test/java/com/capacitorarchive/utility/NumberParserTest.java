package com.capacitorarchive.utility;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class NumberParserTest {

    @Test
    void integer() {
        assertEquals(new BigDecimal("-24"), NumberParser.parse("-24"));
    }

    @Test
    void floatingPoint() {
        assertEquals(new BigDecimal("21.46"), NumberParser.parse("21.46"));
    }

    @Test
    void floatingPointTrailingZeros() {
        assertEquals(new BigDecimal("21.460000"), NumberParser.parse("21.460000"));
    }

    @Test
    void floatingPointLeadingZeros() {
        assertEquals(new BigDecimal("00021.46"), NumberParser.parse("00021.46"));
    }


    @Test
    void nonRepeatingFraction() {
        assertEquals(new BigDecimal("0.5"), NumberParser.parse("1/2"));
    }

    @Test
    void repeatingFraction() {
        assertEquals(new BigDecimal("0.33333333333333333333"), NumberParser.parse("1/3"));
    }

    @Test
    void negativeFraction() {
        assertEquals(new BigDecimal("-0.5"), NumberParser.parse("-1/2"));
    }

    @Test
    void fractionSpaces() {
        assertEquals(new BigDecimal("0.5"), NumberParser.parse("1   /  2"));
    }

    @Test
    void improperRepeatingFraction() {
        assertEquals(new BigDecimal("2.33333333333333333333"), NumberParser.parse("7/3"));
    }

    @Test
    void mixedNumber() {
        assertEquals(new BigDecimal("1.5"), NumberParser.parse("1 1/2"));
    }

    @Test
    void mixedNumberSpaces() {
        assertEquals(new BigDecimal("1.5"), NumberParser.parse("1 1   /  2"));
    }

    @Test
    void mixedNumberRepeating() {
        assertEquals(new BigDecimal("13.14285714285714285714"), NumberParser.parse("13 1/7"));
    }

    @Test
    void negativeMixedNumber() {
        assertEquals(new BigDecimal("-3.25"), NumberParser.parse("-3 1/4"));
    }

    @Test
    void positiveMixedNumber() {
        assertEquals(new BigDecimal("3.25"), NumberParser.parse("+3 1/4"));
    }

    @Test
    void mixedNumberMinusSpace() {
        assertEquals(new BigDecimal("3.25"), NumberParser.parse("3 - 1/4"));
    }

    @Test
    void mixedNumberMinus() {
        assertEquals(new BigDecimal("3.25"), NumberParser.parse("3-1/4"));
    }

    @Test
    void negativeMixedNumberMinus() {
        assertEquals(new BigDecimal("-3.25"), NumberParser.parse("-3 - 1/4"));
    }

    @Test
    void negativeMixedNumberMinusSpace() {
        assertEquals(new BigDecimal("-3.25"), NumberParser.parse("-3 - 1 /   4"));
    }

    @Test
    void tooManyMinuses__fail() {
        assertThrows(NumberFormatException.class, () -> NumberParser.parse("-3 -- 1 /   4"));
    }

    @Test
    void doubleNegative__fail() {
        assertThrows(NumberFormatException.class, () -> NumberParser.parse("--3 - 1 /   4"));
    }



}
