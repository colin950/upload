package com.example.calculate.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.calculate.security.UserInfo;
import com.example.calculate.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomServletWrappingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(UUID.randomUUID().toString(),
                    userInfo, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        if (request.getContentType() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getContentType().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            filterChain.doFilter(request, response);
        } else {
            MultiReadableHttpServletRequest wrappingRequest =
                    new MultiReadableHttpServletRequest(request);
            ContentCachingResponseWrapper wrappingResponse =
                    new ContentCachingResponseWrapper(response);

            filterChain.doFilter(wrappingRequest, wrappingResponse);
            wrappingResponse.copyBodyToResponse();
        }

    }
}
