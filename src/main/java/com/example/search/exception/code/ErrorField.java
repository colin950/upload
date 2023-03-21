package com.example.search.exception.code;

import lombok.Data;

@Data
public class ErrorField {
    private String field;
    private ErrorFieldType type;
    private int min;
    private int max;
}
