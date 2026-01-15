package it.eforhum.backend.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // after the CORSsssss filter
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(formatter);

        logger.info("=== Incoming Request ===");
        logger.info("Timestamp: {}", timestamp);
        logger.info("Method: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        if (httpRequest.getQueryString() != null) {
            logger.info("Query: {}", httpRequest.getQueryString());
        }
        logger.info("Client IP: {}", httpRequest.getRemoteAddr());
        logger.info("User-Agent: {}", httpRequest.getHeader("User-Agent"));

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("=== Response ===");
            logger.info("Status: {} | Duration: {} ms", httpResponse.getStatus(), duration);
            logger.info("========================");
        }
    }
}
