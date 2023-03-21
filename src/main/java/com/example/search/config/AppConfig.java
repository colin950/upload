package com.example.search.config;

import com.example.search.base.code.LangCd;
import com.example.search.component.CustomLocaleResolver;
import com.example.search.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public LocaleResolver localeResolver() {
        CustomLocaleResolver localeResolver = new CustomLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        localeResolver.setSupportedLocales(
                Arrays.stream(LangCd.values())
                        .map(x -> UserUtils.convertStringToLocale(x.name()))
                        .collect(Collectors.toList()));
        return localeResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        Locale.setDefault(Locale.KOREA);

        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(180);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
