package com.example.calculate.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.calculate.constants.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserSecurityContextRepository implements SecurityContextRepository {



    private final UserAuthenticationProvider authenticationProvider;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String authHeader = request.getHeader(Constant.TOKEN_HEADER);
        if (authHeader != null && authHeader.startsWith(Constant.TOKEN_PREFIX)) {
            String authToken = authHeader.substring(7);
            Authentication auth = new UsernamePasswordAuthenticationToken(request, authToken);
            try {
                Authentication authentication = this.authenticationProvider.authenticate(auth);
                return new SecurityContextImpl(authentication);
            } catch (ExpiredJwtException e) {
                log.debug(String.format("expired token Claims : $s", e.getClaims()));
                return new SecurityContextImpl(); //empty
            } catch (MalformedJwtException | UnsupportedJwtException e) {
                log.warn("bad token Request : '{}'", e.getMessage());
                return new SecurityContextImpl(); //empty
            }
        } else {
            return new SecurityContextImpl(); //empty
        }
    }

    @Override
    public void saveContext(
            SecurityContext context,
            HttpServletRequest request,
            HttpServletResponse response) {}

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }
}
