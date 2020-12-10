package com.example.demo.annotation;


import com.example.demo.validator.NumbersInOrderValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumbersInOrderValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NumbersInOrder {

    String message() default "secondNumber is less than firstNumber";

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String firstNumber();

    String secondNumber();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        NumbersInOrder[] value();
    }
}
