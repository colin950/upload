package com.example.search.exception;

import com.example.search.exception.code.ErrorCode;

import java.util.Objects;

public class InternalErrorException extends RuntimeException {
    protected String code = "internal_error";
    protected String[] args;

    public InternalErrorException(String code) {
        super();
        if (!Objects.equals(code, "")) {
            this.code = code;
        }
    }

    public InternalErrorException(String code, String message) {
        super(message);
        if (!Objects.equals(code, "")) {
            this.code = code;
        }
    }

    public InternalErrorException(String code, Throwable cause) {
        super(cause);
        if (!Objects.equals(code, "")) {
            this.code = code;
        }
    }

    public InternalErrorException(String code, String message, Throwable cause) {
        super(message, cause);
        if (!Objects.equals(code, "")) {
            this.code = code;
        }
    }

    public InternalErrorException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        if (!Objects.equals(code, "")) {
            this.code = errorCode.name();
        }
    }

    public InternalErrorException(ErrorCode errorCode, String addMsg) {
        super(errorCode.getMsg() + "(" + addMsg + ")");
        if (!Objects.equals(code, "")) {
            this.code = errorCode.name();
        }
    }

    public InternalErrorException(ErrorCode errorCode, String[] args) {
        super(errorCode.getMsg());
        if (!Objects.equals(code, "")) {
            this.code = errorCode.name();
        }
        if (!Objects.isNull(args)) {
            this.args = args;
        }
    }

    public String getCode() {
        return code;
    }

    public String[] getArgs() {
        return args;
    }
}
