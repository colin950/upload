package com.example.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * 추후 다른 audit이 필요할경우 사용예정
 */
@Configuration
@EnableJpaAuditing(
        dateTimeProviderRef = "auditingDateTimeProvider")
public class AuditingConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {return () -> Optional.of(LocalDateTime.now());}
}
