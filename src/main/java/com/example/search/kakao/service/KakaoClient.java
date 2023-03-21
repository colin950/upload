package com.example.search.kakao.service;

import com.example.search.base.service.RestService;
import com.example.search.exception.UnprocessableException;
import com.example.search.kakao.dto.SearchKakaoKeywordDto;
import com.example.search.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoClient {
    @Value("${kakao.base_url}")
    private String baseUrl;

    @Value("${kakao.access_token}")
    private String accessToken;

    private final String SEARCH_KEYWORD = "/v2/local/search/keyword.json";

    private final RestService restService;

    private String KakaoAK = "KakaoAK ";

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, KakaoAK + accessToken);
        return headers;
    }

    public SearchKakaoKeywordDto getSearchByKeyWord(String keyword, int size) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("query", keyword);
            params.add("size", String.valueOf(size));
            String url = restService.generateQueryUrl(String.format("%s%s", baseUrl, SEARCH_KEYWORD), params);
            Mono<?> res = restService.getCall(url, defaultHeaders());
            String resMsg = (String) res.block();

            return MapperUtils.fromJson(resMsg, SearchKakaoKeywordDto.class);
        } catch (Exception e) {
            throw new UnprocessableException(e.getMessage());
        }
    }
}
