package com.example.upload.base.code.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.upload.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator implements ConstraintValidator<PatternCustom, String> {
    private PatternCustom annotation;

    @Override
    public void initialize(PatternCustom constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        // 핸드폰은 "-" 삭제 
        if (annotation.type().equals(PatternTypeCode.phone)) {
            value = value.replace("-", "");
        }
        Pattern pattern = Pattern.compile(annotation.type().getRegexp());
        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
