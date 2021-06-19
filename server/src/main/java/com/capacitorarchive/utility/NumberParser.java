package com.capacitorarchive.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

/**
 * A package to convert a wide range of string-number representations into BigDecimal objects.
 */
public class NumberParser {

    private final static Pattern DECIMAL = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
    private final static Pattern FRACTION = Pattern.compile("[+-]?\\d+\\s*/\\s*\\d+");
    private final static Pattern MIXED_NUMBER = Pattern.compile("(?:[+-]?\\d+((\\s*-\\s*)|\\s+))?\\d+\\s*/\\s*\\d+");
    private final static String MIXED_NUMBER_SEPARATOR = "(\\s*-\\s*)|\\s+";
    private final static String FRACTION_SEPARATOR = "\\s*/\\s*";

    private final static int FRACTION_PRECISION = 20;

    /**
     * Parses number strings that in the format of an integer, floating point, fraction or mixed number.
     * Repeating decimals are rounded to 20 places.
     * @param numStr a string representation of a number,
     *               i.e. "-3346", "15.36", "-1/2", "3 / 4", "21 3/4", "4 - 1/8"
     * @return a decimal representation of the input string.
     * @throws NumberFormatException thrown if the input string can't be parsed
     */
    public static BigDecimal parse(String numStr) throws NumberFormatException {

        BigDecimal result;

        String str = numStr.strip();

        if (DECIMAL.matcher(str).matches()) {
            result = new BigDecimal(str);

        } else if (FRACTION.matcher(str).matches()) {
            result = parseFraction(str);

        } else if (MIXED_NUMBER.matcher(str).matches()) {
            result = parseMixedNumber(str);

        } else {
            throw new NumberFormatException(String.format("For input string: \"%s\"", numStr));
        }

        return result;
    }

    public static BigDecimal parseFraction(String fracStr) {
        String[] frac = fracStr.strip().split(FRACTION_SEPARATOR);

        BigDecimal numerator = new BigDecimal(frac[0]);
        BigDecimal denominator = new BigDecimal(frac[1]);

        return numerator.divide(denominator, FRACTION_PRECISION, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal parseMixedNumber(String mixedNumStr) {
        boolean negative = mixedNumStr.startsWith("-");
        if (negative) {
            mixedNumStr = mixedNumStr.substring(1);
        }

        String wholeNumStr = mixedNumStr.split(FRACTION_SEPARATOR)[0].split(MIXED_NUMBER_SEPARATOR)[0];
        String[] mixedNumberArr = mixedNumStr.replaceAll("\\s*/\\s*", "/").split(MIXED_NUMBER_SEPARATOR);
        String fractionStr = mixedNumberArr[mixedNumberArr.length - 1];

        BigDecimal wholeNum = new BigDecimal(wholeNumStr);
        BigDecimal fraction = parseFraction(fractionStr);


        return negative ? wholeNum.add(fraction).negate() : wholeNum.add(fraction);
    }

}
