package com.example.demo.annotation;


import com.example.demo.validator.ManufacturerValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ManufacturerValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface YearsInOrder {

    String message() default "endYear is before startYear";

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String startYear();

    String endYear();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        YearsInOrder[] value();
    }
}
