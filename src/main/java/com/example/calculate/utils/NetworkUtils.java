package com.example.calculate.utils;

import jakarta.servlet.http.HttpServletRequest;
import com.example.calculate.constants.Constant;

public class NetworkUtils {

    public static String getUserAgent(HttpServletRequest req) {
        return req.getHeader(Constant.USER_AGENT);
    }

}
