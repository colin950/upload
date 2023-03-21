package com.example.search.base.code;

import lombok.Getter;

@Getter
public enum TelecomType implements IEnumCode {
    SKT("01", "SKT"),
    KT("02", "KT"),
    LGT("03", "LGT");

    private String code;
    private String desc;

    TelecomType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
