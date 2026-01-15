package it.eforhum.backend.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import it.eforhum.backend.exception.UnauthenticatedException;
import it.eforhum.backend.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthFilter(AuthService authService, HandlerExceptionResolver handlerExceptionResolver) {
        this.authService = authService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/weddings")) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                handlerExceptionResolver.resolveException(request, response, null,
                        new UnauthenticatedException("Missing or invalid Authorization header"));
                return;
            }

            String token = authHeader.substring(7);
            var userOpt = authService.validateToken(token);

            if (userOpt.isEmpty()) {
                handlerExceptionResolver.resolveException(request, response, null,
                        new UnauthenticatedException("Invalid or expired token"));
                return;
            }

            request.setAttribute("currentUser", userOpt.get());
        }

        filterChain.doFilter(request, response);
    }
}
