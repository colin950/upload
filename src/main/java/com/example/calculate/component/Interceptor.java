package com.example.calculate.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {

    // controller로 보내기 전에 처리하는 인터셉터
    // 반환이 false라면 controller로 요청을 안함
    // 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler )
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object obj)
            throws Exception {
        log.info("preHandler");
        return true;
    }

    // controller의 handler가 끝나면 처리됨
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object obj,
            ModelAndView mav)
			throws Exception {
        log.info("postHandler");
    }

    // view까지 처리가 끝난 후에 처리됨
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object obj,
            Exception e)
            throws Exception {
        log.info("afterCompletion");
    }

}
