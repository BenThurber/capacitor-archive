package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service("textUtilService")
public class TextUtil {


    public static String title(String str) {
        return title(str, new Character[]{' '});
    }

    /**
     * Takes a string and capitalizes the first letter between each deliminator
     * @param str the string to make a title
     * @param deliminators An array of characters.  This could be [' ', '-'] that would capitalize words separated by
     *                     spaces and dashes.
     * @return capitalized string
     */
    public static String title(String str, Character[] deliminators) {
        Set<Character> deliminatorSet = new HashSet<>(Arrays.asList(deliminators));
        char[] chars = str.toLowerCase().toCharArray();
        boolean previousCharIsLetter = false;
        for (int i = 0; i < chars.length; i++) {
            if (!previousCharIsLetter && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                previousCharIsLetter = true;
            } else if (deliminatorSet.contains(chars[i])) {
                previousCharIsLetter = false;
            }
        }
        return String.valueOf(chars);
    }
}
