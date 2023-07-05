package com.example.upload.base.code.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {PatternValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
// runtime까지 유지시킴
@Retention(RetentionPolicy.RUNTIME)
public @interface PatternCustom {
    String message() default  "invalid pattern value";

    Class<?>[] group() default {};

    Class<? extends Payload>[] payload() default {};

    PatternTypeCode type();
}
