package com.example.search.utils;

import jakarta.servlet.http.HttpServletRequest;
import com.example.search.base.code.LangCd;
import com.example.search.constants.Constant;
import com.example.search.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

@Slf4j
public class UserUtils {
    public static UserInfo getUserInfo() { return getUserInfo(false); }

    public static UserInfo getUserInfo(boolean nullable) {
        boolean anonymous = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            anonymous = true;
        }
        UserInfo userInfo = anonymous ? new UserInfo() : (UserInfo) authentication.getPrincipal();
        return userInfo;

    }

    public static UserInfo getUserInfo(HttpServletRequest request) {
        PlatformInfo platformInfo = RequestUtils.getPlatformInfo(request);
        UserInfo userInfo = new UserInfo()
                .setUserAgent(NetworkUtils.getUserAgent(request))
                .setPlatformInfo(platformInfo);

        String langCd = request.getHeader(Constant.LANGUAGE_HEADER);
        if (StringUtils.isEmpty(langCd)) {
            langCd = LangCd.ko.name();
        }
        userInfo.setLangCd(langCd);
        String requestId = request.getHeader(Constant.REQUEST_ID_HEADER);
        if (StringUtils.isEmpty(requestId)) {
            userInfo.setRequestId(requestId);
        } else {
            userInfo.setRequestId(Constant.NONE);
        }
        return userInfo;
    }

    public static Locale convertStringToLocale(String lang) {
        Locale locale = null;
        if (StringUtils.isEmpty(lang)) {
            locale = locale.getDefault();
        } else {
            if (lang.indexOf(",") > -1) {
                lang = lang.substring(0,2);
            }
            locale = new Locale(lang);
        }
        return locale;
    }
}
