package com.example.upload.push.service;

import com.example.upload.base.service.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PushClient {

//    @Value("localhost:8082")
    private String baseUrl;

    private final RestService restService;

    public void push() {
        // restService(webClient를 통한 호출 및 queue전송)
    }
}
