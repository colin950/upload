package com.example.calculate.base.code;

import lombok.Getter;

@Getter
public enum CalculateType implements IEnumCode {
    AUTO("01", "auto"),
    CUSTOM("02", "custom");

    private String code;
    private String desc;

    CalculateType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}