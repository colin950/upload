package com.example.search.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.search.exception.code.ErrorCode;
import com.example.search.exception.code.ErrorTitle;
import com.example.search.utils.MapperUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponse err = new ErrorResponse(ErrorCode.DEMO_COMMON_E001);
        err.setTitle(ErrorTitle.system_error.name());
        response.getWriter().print(MapperUtils.toJson(err));
    }
}
