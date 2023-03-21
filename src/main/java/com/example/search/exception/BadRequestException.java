package com.example.search.exception;

import com.example.search.exception.code.ErrorCode;

public class BadRequestException extends InternalErrorException{

    public BadRequestException(String code) {
        super(code);
    }

    public BadRequestException(String code, String message) {
        super(code, message);
    }

    public BadRequestException(String code, Throwable cause) {
        super(code, cause);
    }

    public BadRequestException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String addMsg) {
        super(errorCode, addMsg);
    }

    public BadRequestException(ErrorCode errorCode, String[] args) {
        super(errorCode, args);
    }
}
