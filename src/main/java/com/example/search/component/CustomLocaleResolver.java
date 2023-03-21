package com.example.search.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.search.constants.Constant;
import com.example.search.utils.StringUtils;
import com.example.search.utils.UserUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomLocaleResolver implements LocaleResolver {

    private Locale defaultLocale = Locale.KOREA;

    private final List<Locale> supportedLocales = new ArrayList<>();

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        String langCd = request.getHeader(Constant.LANGUAGE_HEADER);
        if (defaultLocale != null && StringUtils.isEmpty(langCd)) {
            return defaultLocale;
        }
        Locale headerLocale = UserUtils.convertStringToLocale(langCd);
        if (!getSupportedLocales().contains(headerLocale)) {
            return defaultLocale;
        }
        return headerLocale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {}
    public List<Locale> getSupportedLocales() { return supportedLocales; }

    public void setSupportedLocales(List<Locale> supportedLocales) {
        this.supportedLocales.clear();
        this.supportedLocales.addAll(supportedLocales);
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}
