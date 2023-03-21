package com.example.search.config;

import jakarta.servlet.http.HttpServletResponse;
import com.example.search.exception.AccessDeniedExceptionHandler;
import com.example.search.exception.ErrorResponse;
import com.example.search.exception.code.ErrorCode;
import com.example.search.exception.code.ErrorTitle;
import com.example.search.security.UserAuthenticationProvider;
import com.example.search.security.UserSecurityContextRepository;
import com.example.search.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *  Authentication 구조
 *  1. securityContextHolder - (인증보관함 보관소)
 *  ###사이에 security filter, authenticationProvider (인증 제공자) 존재함###
 *  2. securityContext - (인증보관함)
 *      2-1. Authentication - (인증)
 *      2-2. Principle (UserDetails) 인증 대상
 *      2-3. GrantedAuthority 권한
 * https://velog.io/@seongwon97/Spring-Boot-Authentication%EC%9D%98-%EA%B5%AC%EC%A1%B0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserAuthenticationProvider authenticationProvider;

    private final UserSecurityContextRepository securityContextRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/actuator/**"),
                new AntPathRequestMatcher("/v1/search/**"));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .disable()
//                .configurationSource(corsConfigurationSource())
//                .and()
                //Cross site Request forgery로 사이즈간 위조 요청인데, 즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것
                // jwt 같은 인증수단을 사용하기 때문에 안씀
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .logout()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedExceptionHandler())
                .and()
                .authenticationProvider(authenticationProvider)
                .securityContext()
                .securityContextRepository(securityContextRepository)
                .and()
                .sessionManagement()
                // 무슨 속성값일까?
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/**/mfa/**"))
                .hasRole("MASTER")
                .anyRequest()
                .denyAll();
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return ((request, response, authException) -> {
            if (authException instanceof AccountExpiredException) {
                // log
            }
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponse err = new ErrorResponse(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR);
            err.setTitle(ErrorTitle.system_error.name());
            response.getOutputStream().println(MapperUtils.toJson(err));
        });
    }

    @Bean
    public PasswordEncoder encoder() { return new BCryptPasswordEncoder(); }
}
