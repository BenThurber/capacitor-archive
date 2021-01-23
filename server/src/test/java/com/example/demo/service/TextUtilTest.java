package com.example.demo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextUtilTest {

    @Test
    void emptyString() {
        assertEquals("", TextUtil.title(""));
    }

    @Test
    void singleSpace() {
        assertEquals(" ", TextUtil.title(" "));
    }

    @Test
    void onlyDeliminators() {
        assertEquals(" - -- ", TextUtil.title(" - -- ", new Character[]{' ', '-'}));
    }

    @Test
    void singleWord() {
        assertEquals("Foobar", TextUtil.title("foOBar"));
    }

    @Test
    void twoWords() {
        assertEquals("Hello World", TextUtil.title("heLLo woRld"));
    }

    @Test
    void multipleWords() {
        assertEquals("The Quick Brown Fox", TextUtil.title("the Quick brown fox"));
    }

    @Test
    void multipleWordsMultipleDeliminators() {
        assertEquals("The-Quick_Brown Fox", TextUtil.title("the-Quick_brown fox", new Character[]{' ', '-', '_'}));
    }

}
