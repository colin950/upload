package com.example.calculate.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.calculate.exception.code.ErrorCode;
import com.example.calculate.exception.code.ErrorField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String code;
    private String title;
    private String message;

    private List<ErrorField> errors;
    @JsonIgnore private String[] args;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.name();
        this.title = "error";
        this.message = errorCode.getMsg();
        this.args = null;
    }

    public ErrorResponse(ErrorCode errorCode, List<ErrorField> errorFields) {
        this.code = errorCode.name();
        this.title = "error";
        this.message = errorCode.getMsg();
        this.errors = errorFields;
        this.args = null;
    }

    public ErrorResponse(String code, String message, String[] args) {
        this.code = code;
        this.title = "error";
        this.message = message;
        this.args = args;
    }
}
