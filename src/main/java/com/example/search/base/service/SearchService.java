package com.example.search.base.service;

import com.example.search.base.dto.response.SearchPlaceResponseDto;
import com.example.search.entity.SearchCntBaseEntity;
import com.example.search.kakao.dto.SearchKakaoKeywordDetailsDto;
import com.example.search.kakao.dto.SearchKakaoKeywordDto;
import com.example.search.kakao.service.KakaoClient;
import com.example.search.naver.dto.SearchNaverKeywordDetailsDto;
import com.example.search.naver.dto.SearchNaverKeywordDto;
import com.example.search.naver.service.NaverClient;
import com.example.search.repository.SearchCntBaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;

    private final SearchCntBaseRepository searchCntBaseRepository;

    private final ApplicationContext context;

    private final int kakaoSearchSize = 5;
    private final int naverSearchSize = 5;

    // 순서 보장을 위한 List 사용
    public List<SearchPlaceResponseDto> searchPlaces(String keyword) {
        // 조회 (default 5개 설정)
        SearchNaverKeywordDto searchNaver = naverClient.getSearchByKeyWord(keyword);
        int addSearchCnt = 0;
        if (searchNaver != null) {
            addSearchCnt = naverSearchSize - searchNaver.getItems().size();
        }
        // naver에서 조회시 5개 이하의 경우 카카오 추가 더 검색
        SearchKakaoKeywordDto searchKakao = kakaoClient.getSearchByKeyWord(keyword, kakaoSearchSize + addSearchCnt);

        // 장소명을 비교 하기 위한 List 재조합
        List<String> kakaoPlaceNameList = searchKakao.getDocuments()
                .stream()
                .map(SearchKakaoKeywordDetailsDto::getPlace_name)
                .collect(Collectors.toList());

        List<String> naverPlaceNameList = searchNaver.getItems()
                .stream()
                .map(SearchNaverKeywordDetailsDto::getTitle)
                .collect(Collectors.toList());

        // 기준점이 될 리스트, 비교 리스트
        // response 순서로 정렬 진행
        // 1. 모두 존재
        // 2. main 우선 정렬
        // 3. sub 정렬
        List<String> listConcat = sortSearchResult(kakaoPlaceNameList, naverPlaceNameList);

        List<SearchPlaceResponseDto> res = new ArrayList<>();
        listConcat.stream().forEach(
                item -> {
                    res.add(
                            SearchPlaceResponseDto.builder()
                                    .placeName(item)
                                    .build());
                });
        // proxy 사용
        getProxy().updateKeywordCnt(keyword);
        return res;
    }

    public List<String> sortSearchResult(List<String> mainList, List<String> subList) {
        // 카카오, 네이버 결과 모두 존재 하는 List 추출
        List<String> sameExists = mainList.stream()
                .filter(x -> subList.contains(x))
                .collect(Collectors.toList());

        List<String> listConcat = Stream.concat(
                        mainList.stream(),
                        subList.stream())
                .distinct()
                .sorted( // 카카오 네이버 결과 모두 존재하는 List 우선 정렬
                        Comparator.comparing(sameExists::contains)
                                // 카카오 에 존재하는 리스트 그 다음 정렬
                                .thenComparing(mainList::contains).reversed())
                .collect(Collectors.toList());
        return listConcat;
    }


    // 조회를 넣게 될 경우 중간에 구간이 생길 수 있으므로 해당 방법으로 처리
    // 동시성 이슈를 위해 해당 메소드 진입시에 insert만 진행
    // keyword에 unique를 잡고 해당 데이터 중복 에러 발생시 update 진행
    public void updateKeywordCnt(String keyword) {
        try {
            SearchCntBaseEntity searchCntBaseEntity = new SearchCntBaseEntity()
                    .setKeyword(keyword)
                    .setCnt(1L);
            searchCntBaseRepository.save(searchCntBaseEntity);
        } catch (DataIntegrityViolationException e) {
            int isSuccessCnt = searchCntBaseRepository.updateSearchCnt(keyword);
            if (isSuccessCnt < 1) {
                log.error(String.format("[updateKeywordCnt] error : %s", e.getMessage()));
            }
        } catch (Exception e) {
            // 다른 이유로 해당 데이터를 업데이트 하지 못할 경우에는 에러로 처리해야하는가?
            log.error(e.toString());
        }

    }

    @Transactional(readOnly = true)
    public Page<SearchCntBaseEntity> findByKeyword(Pageable pageable) {
        return searchCntBaseRepository.findAll(pageable);
    }

    // proxy를 가져와서 사용
    // transaction 중간에 사용시에 사용
    private SearchService getProxy() { return context.getBean(SearchService.class); }
}
