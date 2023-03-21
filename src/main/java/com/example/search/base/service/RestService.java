package com.example.search.base.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestService {
    private final WebClient webClient;

    public String generateQueryUrl(String url, MultiValueMap<String, String> params) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(URI.create(url)).queryParams(params).build();
        return uriComponents.toUriString();
    }

    public Mono<?> getCall(String url, HttpHeaders addHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE
        );
        headers.addAll(addHeaders);
        return get(url, headers);
    }

    public Mono<?> getCall(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE
        );
        return get(url, headers);
    }

    private Mono<?> get(String url, HttpHeaders headers) {
        StringBuffer req = new StringBuffer();
        req.append("url : ").append(url).append("method : ").append("get");
        return webClient
                .mutate()
                .baseUrl(url)
                .build()
                .get()
                .headers(h -> {h.addAll(headers);})
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    //TODO logging ?
                    return Mono.error(e);
                })
                .onErrorResume(Exception.class, e -> {
                    e.printStackTrace();
                    return Mono.error(e);
                });
    }
}
