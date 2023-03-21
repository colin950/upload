package com.example.search.naver.service;

import com.example.search.base.service.RestService;
import com.example.search.naver.dto.SearchNaverKeywordDto;
import com.example.search.utils.MapperUtils;
import com.example.search.utils.StringUtils;
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
public class NaverClient {
    @Value("${naver.base_url}")
    private String baseUrl;

    @Value("${naver.client_id}")
    private String clientId;

    @Value("${naver.client_secret}")
    private String clientSecret;

    private final String SEARCH_KEYWORD = "/v1/search/local.json";

    private final String XNaverClientId = "X-Naver-Client-Id";
    private final String XNaverClientSecret = "X-Naver-Client-Secret";

    private int defaultDisplay = 5;

    private final RestService restService;

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(XNaverClientId, clientId);
        headers.add(XNaverClientSecret, clientSecret);
        return headers;
    }

    public SearchNaverKeywordDto getSearchByKeyWord(String keyword) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            // utf-8 변환하여 사용
            params.add("query", StringUtils.convertUTF8(keyword));
            params.add("display", String.valueOf(defaultDisplay));
            String url = restService.generateQueryUrl(String.format("%s%s", baseUrl, SEARCH_KEYWORD), params);
            Mono<?> res = restService.getCall(url, defaultHeaders());
            String resMsg = (String) res.block();

            return MapperUtils.fromJson(resMsg, SearchNaverKeywordDto.class);
        } catch (Exception e) {
            log.error(String.format("[getSearchByKeyWord] naver error : %s", e.getMessage()));
            return new SearchNaverKeywordDto();
        }
    }
}
