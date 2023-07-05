package com.example.upload.utils;


import org.slf4j.Logger;

public class LogUtils {
    private static final String SERVICE_PREFIX = "demo";

    /** [requestId][SERVICE_PREFIX] message **/
    public static void debug(Logger log, Object... arguments) {
        log.debug(
                "[{}][{}]{}",
                UserUtils.getUserInfo().getRequestId(),
                SERVICE_PREFIX,
                log.getName(),
                arguments);
    }

}
