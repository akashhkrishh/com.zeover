package com.zeogrid.zeover.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeogrid.zeover.model.User;
import com.zeogrid.zeover.payload.response.GlobalResponse;
import com.zeogrid.zeover.repository.UserRepository;
import com.zeogrid.zeover.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final ApplicationContext applicationContext;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTFilter(ApplicationContext applicationContext, JWTService jwtService) {
        this.applicationContext = applicationContext;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        String userId;

        try {
            userId = jwtService.extractUserID(token);
        } catch (Exception e) {
            writeUnauthorizedResponse(response, "Malformed or invalid JWT: " + e.getMessage());
            return;
        }

        if (userId == null) {
            writeUnauthorizedResponse(response, "Invalid token or user ID not found.");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userOpt = applicationContext
                    .getBean(UserRepository.class)
                    .findById(UUID.fromString(userId));

            if (userOpt.isEmpty()) {
                writeUnauthorizedResponse(response, "User not found.");
                return;
            }

            User user = userOpt.get();

            try {
                if (!jwtService.validateToken(token, user)) {
                    writeUnauthorizedResponse(response, "Invalid token.");
                    return;
                }
            } catch (Exception e) {
                writeUnauthorizedResponse(response, "Token validation error: " + e.getMessage());
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        GlobalResponse<Object> globalResponse = new GlobalResponse<>(
                null,
                "Invalid JWT token",
                errorMessage,
                false
        );

        String jsonResponse = objectMapper.writeValueAsString(globalResponse);
        response.getWriter().write(jsonResponse);
    }
}
