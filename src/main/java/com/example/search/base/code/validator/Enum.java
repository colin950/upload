package com.example.search.base.code.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotation 실행할 구현체를 enumvalidator로 지정
@Constraint(validatedBy = {EnumValidator.class})
// 해당 어노테이션은 메소드 필드 파라미터에 적용 가능
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
// runtime까지 유지시킴
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {
    String message() default "Invalid enum value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends java.lang.Enum<?>> enumClass();

    boolean ignoreCase() default false;

    boolean codeCompare() default false;


}
