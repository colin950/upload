package com.example.search.service;

import com.example.search.base.service.SearchService;
import com.example.search.entity.SearchCntBaseEntity;
import com.example.search.repository.SearchCntBaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("검색 조회 테스트")
@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchCntBaseRepository repository;

    @Test
    public void 검색_조건_정렬_테스트() {
         //장소명을 비교 하기 위한 List 재조합
        List<String> mainList = new ArrayList<>(Arrays.asList("e", "b", "c", "d"));
        List<String> subList = new ArrayList<>(Arrays.asList("a", "d", "f", "q"));

        // 기준점이 될 리스트, 비교 리스트
        // response 순서로 정렬 진행
        // 1. 모두 존재
        // 2. main 우선 정렬
        // 3. sub 정렬
        List<String> res = searchService.sortSearchResult(mainList, subList);

        assertNotNull(res);
        // d의 경우 모두 존재함으로 우선 배치
        assertEquals("d", res.get(0));
    }

    @Test
    public void 생성시간_업데이트시간_테스트() {
        // auditor 설정 반영 확인 테스트
        SearchCntBaseEntity searchCntBaseEntity = new SearchCntBaseEntity();
        searchCntBaseEntity.setCnt(1L);
        searchCntBaseEntity.setKeyword("test");
        repository.save(searchCntBaseEntity);

        List<SearchCntBaseEntity> searchList = repository.findAll();
        SearchCntBaseEntity search = searchList.get(0);

        System.out.println(">>createdDate="+ search.getCreatedAt() + ", modifiedDate=" + search.getUpdatedAt());
        assertNotNull(search.getCreatedAt());
        assertNotNull(search.getUpdatedAt());
    }

}
