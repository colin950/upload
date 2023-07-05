package com.example.upload.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import com.example.upload.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies =
                ExchangeStrategies.builder()
                        .codecs(
                                configurer ->
                                        configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 5000))
                        .build();
        exchangeStrategies.messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(
                        writer ->
                                ((LoggingCodecSupport) writer)
                                        // 요청 로깅 상세 설정
                                        .setEnableLoggingRequestDetails(true));
        return WebClient.builder()
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                                        .responseTimeout(Duration.ofMillis(5000))
                                        .doOnConnected(con ->
                                                con.addHandlerFirst(new ReadTimeoutHandler(5))
                                                        .addHandlerFirst(new WriteTimeoutHandler(60)))))
                .exchangeStrategies(exchangeStrategies)
                .filter(
                        ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                            LogUtils.debug(
                                    log,
                                    "[REQUEST]: {} {}",
                                    clientRequest.method(),clientRequest.url());
                            return Mono.just(clientRequest);
                        }))
                .filter(
                        ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                            clientResponse
                                    .headers()
                                    .asHttpHeaders()
                                    .forEach(
                                            (name, values) ->
                                                    values.forEach(
                                                            value ->
                                                                    LogUtils.debug(
                                                                            log,
                                                                            "{} {}",
                                                                            name, value)));
                            return Mono.just(clientResponse);
                        }))
                .build();

    }
}
