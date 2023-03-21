package com.example.search.component;

import com.example.search.base.code.LangCd;
import com.example.search.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class I18nMessage {
    private final MessageSource messageSource;

    public String getMessage(String code) { return getMessage(code, null); }

    public String getMessage(String code, Object[] args, LangCd langCd) {
        Locale locale = UserUtils.convertStringToLocale(langCd.name());
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
