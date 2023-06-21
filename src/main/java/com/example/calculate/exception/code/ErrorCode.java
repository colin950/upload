package com.example.calculate.exception.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    DEMO_COMMON_E001("access_denied"),
    DEMO_COMMON_UNKNOWN_ERROR("unknown_error"),
    DEMO_COMMON_INVALID_PARAM("invalid_param"),

    DEMO_CALCULATE_NOT_FOUND("not_found"),
    DEMO_CALCULATE_ALREADY_DONE("already_done"),

    DEMO_CALCULATE_DISAGREE("amount_disagree"),

    DEMO_AUTH_INVALID_EMAIL("invalid_email");

    private String msg;
    ErrorCode(String msg) { this.msg = msg; }
}
