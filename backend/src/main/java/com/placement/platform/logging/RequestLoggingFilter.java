package com.placement.platform.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger("RequestLogging");

    private static final String REQUEST_ID_MDC_KEY = "requestId";
    private static final String USER_ID_MDC_KEY = "userId";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final String[] excludePatterns = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/health",
            "/favicon.ico"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();

        // Check if path is excluded from logging
        for (String pattern : excludePatterns) {
            if (pathMatcher.match(pattern, uri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Generate or retrieve Request ID
        String requestId = httpRequest.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(REQUEST_ID_MDC_KEY, requestId);

        long startTime = System.currentTimeMillis();
        String method = httpRequest.getMethod();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = httpResponse.getStatus();

            // Extract User ID if authenticated
            String userId = "anonymous";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                userId = auth.getName();
            }
            MDC.put(USER_ID_MDC_KEY, userId);

            String queryString = httpRequest.getQueryString();
            String fullUri = queryString != null ? uri + "?" + queryString : uri;

            // Secure request logging - never log Authorization headers, tokens or bodies.
            log.info("Method: {}, URI: {}, Status: {}, Duration: {}ms, User: {}",
                    method, fullUri, status, duration, userId);

            MDC.clear();
        }
    }
}
