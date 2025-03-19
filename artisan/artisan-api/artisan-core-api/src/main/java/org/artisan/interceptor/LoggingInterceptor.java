package org.artisan.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 요청 정보 로그
        return true;
    }

    @Slf4j
    @Component
    public static class RequestLoggingFilter extends OncePerRequestFilter {


        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            // 요청을 ContentCachingRequestWrapper로 감싸서 본문을 캐싱할 수 있도록 함
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

            filterChain.doFilter(wrappedRequest, response);

            // 컨트롤러 호출 후에 요청 본문을 로그로 출력 (바이트 배열을 문자열로 변환)
            byte[] content = wrappedRequest.getContentAsByteArray();
            if (content.length > 0) {
                String requestBody = new String(content, wrappedRequest.getCharacterEncoding());
                log.info("Request URL: {} {}", request.getMethod(), request.getRequestURI());
                log.info("Request Body: {}", requestBody);
            }
        }
    }
}



