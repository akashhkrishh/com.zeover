package com.zeogrid.zeover.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FrontendOnlyFilter extends OncePerRequestFilter {

    @Value("${frontend.api.secret}")
    private String allowedSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/")) {
            String secretHeader = request.getHeader("X-Frontend-Secret");

            if (secretHeader == null || !secretHeader.equals(allowedSecret)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access denied: Invalid or missing X-Frontend-Secret header");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
