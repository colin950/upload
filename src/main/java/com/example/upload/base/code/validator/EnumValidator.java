package com.example.upload.base.code.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.upload.base.code.IEnumCode;
import com.example.upload.utils.StringUtils;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;
    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
           return true;
        }
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                String targetStr = enumValue.toString();
                if (this.annotation.codeCompare()) {
                    targetStr = ((IEnumCode) enumValue).getCode();
                }
                if (value.equals(targetStr)
                        || (this.annotation.ignoreCase() && value.equalsIgnoreCase(targetStr))) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
