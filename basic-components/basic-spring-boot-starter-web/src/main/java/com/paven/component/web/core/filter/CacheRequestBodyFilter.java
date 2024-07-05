package com.paven.component.web.core.filter;

import com.paven.common.util.servlet.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Request Body 缓存 Filter，实现它的可重复读取
 *
 * @author paven
 */
public class CacheRequestBodyFilter extends OncePerRequestFilter {

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new CacheRequestBodyWrapper(request), response);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !ServletUtils.isJsonRequest(request);
    }
}
