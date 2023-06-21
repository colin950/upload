package com.example.calculate.utils;

import jakarta.servlet.http.HttpServletRequest;
import com.example.calculate.constants.Constant;
import com.example.calculate.exception.InvalidParameterException;
import com.example.calculate.exception.code.ErrorFieldType;

import java.util.Objects;

public class RequestUtils {

    public static PlatformInfo getPlatformInfo(final HttpServletRequest request) {
        String clientId = Objects.isNull(request.getHeader(Constant.CLIENT_ID)) ? Constant.NONE : request.getHeader(Constant.CLIENT_ID);

        PlatformInfo platformInfo = new PlatformInfo()
                .setClientId(clientId);
        return platformInfo;
    }

    public static void validatePlatformInfo(PlatformInfo platformInfo) {
        if (StringUtils.isEmpty(platformInfo.getClientId())
                || platformInfo.getClientId().equals(Constant.NONE))
            throw new InvalidParameterException(Constant.CLIENT_ID, ErrorFieldType.empty);
    }
}
