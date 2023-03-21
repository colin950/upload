package com.example.search.naver.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchNaverKeywordDto implements Serializable {
    private Integer display;

    private List<SearchNaverKeywordDetailsDto> items;

}
