package com.example.demo.validator;

import com.example.demo.annotation.NumbersInOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that two numbers (long, int, short) are in sorted order.  i.e. firstNumber <= secondNumber.
 */
public class NumbersInOrderValidator implements ConstraintValidator<NumbersInOrder, Object> {

    private static Log log = LogFactory.getLog(NumbersInOrderValidator.class);

    private String firstFieldName;
    private String secondFieldName;

    public void initialize(NumbersInOrder constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.firstNumber();
        this.secondFieldName = constraintAnnotation.secondNumber();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object firstNumberObj = new BeanWrapperImpl(value)
                .getPropertyValue(firstFieldName);
        Object secondNumberObj = new BeanWrapperImpl(value)
                .getPropertyValue(secondFieldName);


        if (firstNumberObj == null || secondNumberObj == null) {
            return true;
        }

        // Determine the type of Object
        try {
            Number firstNumber = (Number)firstNumberObj;
            Number secondNumber = (Number)secondNumberObj;

            if (firstNumber.equals(firstNumber.longValue()) && secondNumber.equals(secondNumber.longValue())) {
                // It is integer
                return firstNumber.longValue() <= secondNumber.longValue();
            } else {
                // It is floating point
                return firstNumber.doubleValue() <= secondNumber.doubleValue();
            }

        } catch (ClassCastException e) {
            log.error("Can't parse values during validation");
        }
        return false;
    }
}
