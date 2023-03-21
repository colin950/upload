package com.example.search.base.code.validator;

import lombok.Getter;

@Getter
public enum PatternTypeCode {

    email("asdfasdf"),
    phone("");

    private final String regexp;

    PatternTypeCode(String regexp) { this.regexp = regexp; }
}
