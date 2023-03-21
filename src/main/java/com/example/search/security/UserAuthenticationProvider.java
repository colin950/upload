package com.example.search.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import com.example.search.constants.Constant;
import com.example.search.utils.JwtUtils;
import com.example.search.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtils jwtUtils;


    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        HttpServletRequest request = (HttpServletRequest) authentication.getPrincipal();
        Object userAgent = request.getHeader(Constant.USER_AGENT);

        String authToken = authentication.getCredentials().toString();
        Claims claims = null;
        try {
            claims = jwtUtils.getClaimsFromToken(authToken);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }

        String scope = claims.get("scope", String.class);
        String sub = claims.get("sub", String.class);

        UserInfo userInfo = UserUtils.getUserInfo(request);
        userInfo.setUuid(sub);

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        // ex ROLE_ADMIN, ROLE_USER 이렇게 쓰면된다.
        String role = "ROLE_" + scope;
        roles.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(userInfo, null, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
