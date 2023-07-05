package com.example.upload.exception.code;

public enum ErrorFieldType {
    empty, // 필수 누락
    pattern, // 형식 에러
    size, // 길이 에러
    value, // 값 에러
    unknown
}
