package com.poniansoft.shrtly.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    // List of URLs to exclude from token validation
    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/api/public/health",
            "/api/user/register",
            "/api/shopify/auth",
            "/api/shopify/callback"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        // Skip token validation for OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip token validation for excluded URLs
        if (EXCLUDED_URLS.contains(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Exclude all non-API endpoints (short links, dashboard, etc.)
        if (!requestUri.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = request.getHeader("Authorization");

        if (idToken != null && idToken.startsWith("Bearer ")) {
            idToken = idToken.substring(7); // Remove "Bearer " prefix
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                request.setAttribute("userId", decodedToken.getUid()); // Add user ID to request
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid ID token");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing or invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
