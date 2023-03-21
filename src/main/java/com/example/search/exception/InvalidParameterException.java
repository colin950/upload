package com.example.search.exception;

import com.example.search.exception.code.ErrorField;
import com.example.search.exception.code.ErrorFieldType;

public class InvalidParameterException extends RuntimeException {
    protected ErrorField errorField;

    public InvalidParameterException(ErrorField errorField) {this.errorField = errorField;}
    public InvalidParameterException(String field, ErrorFieldType type) {
        this.errorField = new ErrorField();
        this.errorField.setField(field);
        this.errorField.setType(type);
        this.errorField.setMax(0);
        this.errorField.setMin(0);
    }
}
