package com.placement.platform.infrastructure;

import com.placement.platform.logging.RequestLoggingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequestLoggingFilterTest {

    @Test
    public void testFilterLogsAndExcludes() throws Exception {
        RequestLoggingFilter filter = new RequestLoggingFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Test normal path
        when(request.getRequestURI()).thenReturn("/api/profile/me");
        when(request.getMethod()).thenReturn("GET");
        when(response.getStatus()).thenReturn(200);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(MDC.get("requestId")); // MDC must be cleared after filter execution

        // Test excluded path
        reset(chain);
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }
}
