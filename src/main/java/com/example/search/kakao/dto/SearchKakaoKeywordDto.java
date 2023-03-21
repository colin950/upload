package com.example.search.kakao.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchKakaoKeywordDto {
    private List<SearchKakaoKeywordDetailsDto> documents;
}
