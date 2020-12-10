package com.example.demo.validator;

import com.example.demo.annotation.YearsInOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ManufacturerValidator implements ConstraintValidator<YearsInOrder, Object> {

    private static Log log = LogFactory.getLog(ManufacturerValidator.class);

    private String  startYear;
    private String  endYear;

    public void initialize(YearsInOrder constraintAnnotation) {
        this.startYear = constraintAnnotation.startYear();
        this.endYear = constraintAnnotation.endYear();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object startYearValue = new BeanWrapperImpl(value)
                .getPropertyValue(startYear);
        Object endYearValue = new BeanWrapperImpl(value)
                .getPropertyValue(endYear);


        if (startYearValue == null || endYearValue == null) {
            return true;
        }

        try {
            return (Short) startYearValue <= (Short) endYearValue;
        } catch (ClassCastException e) {
            log.error("Can't parse values during validation");
            return false;
        }
    }
}
