package com.example.upload.utils;

import jakarta.servlet.http.HttpServletRequest;
import com.example.upload.constants.Constant;

public class NetworkUtils {

    public static String getUserAgent(HttpServletRequest req) {
        return req.getHeader(Constant.USER_AGENT);
    }

}
