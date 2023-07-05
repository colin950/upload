package com.example.upload.exception;

import com.example.upload.exception.code.ErrorCode;

public class UnAuthorizedException extends InternalErrorException {
    public UnAuthorizedException(String code) {
        super(code);
    }

    public UnAuthorizedException(String code, String message) {
        super(code, message);
    }

    public UnAuthorizedException(String code, Throwable cause) {
        super(code, cause);
    }

    public UnAuthorizedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException(ErrorCode errorCode, String addMsg) {
        super(errorCode, addMsg);
    }

    public UnAuthorizedException(ErrorCode errorCode, String[] args) {
        super(errorCode, args);
    }
}
