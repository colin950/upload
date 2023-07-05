package com.example.upload.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CompletableFutureTest {
    Logger log = LoggerFactory.getLogger(getClass());

    public int 메소드_완료_3초_필요() {
        log.info("메소드_완료_3초_필요() 실행");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 3;
    }

    public int 메소드_완료_5초_필요() {
        log.info("메소드_완료_5초_필요() 실행");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5;
    }


    @Test
    void test() {
        long start = System.currentTimeMillis();

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(this::메소드_완료_3초_필요);
        CompletableFuture<Integer> future5 = CompletableFuture.supplyAsync(this::메소드_완료_5초_필요);

        int b = future5.join();
        log.info("fut5 걸린시간 {}s", MILLISECONDS.toSeconds((System.currentTimeMillis() - start)));

        long start2 = System.currentTimeMillis();
        int a = future3.join();
        log.info("fut3 걸린시간 {}s", MILLISECONDS.toSeconds((System.currentTimeMillis() - start2)));

        long elapsed = System.currentTimeMillis() - start;
        log.info("[전체시간={}s] 결과는 a + b = {}", MILLISECONDS.toSeconds(elapsed), (a + b));
    }
}
