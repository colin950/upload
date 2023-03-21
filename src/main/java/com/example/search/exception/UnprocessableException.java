package com.example.search.exception;

import com.example.search.exception.code.ErrorCode;

public class UnprocessableException extends InternalErrorException {
    public UnprocessableException(String code) {
        super(code);
    }

    public UnprocessableException(String code, String message) {
        super(code, message);
    }

    public UnprocessableException(String code, Throwable cause) {
        super(code, cause);
    }

    public UnprocessableException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnprocessableException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnprocessableException(ErrorCode errorCode, String addMsg) {
        super(errorCode, addMsg);
    }

    public UnprocessableException(ErrorCode errorCode, String[] args) {
        super(errorCode, args);
    }
}
