package org.security.roles;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoleCheckFilter extends OncePerRequestFilter {

    private final RoleCheckService roleCheckService;

    @Autowired
    public RoleCheckFilter(RoleCheckService roleCheckService) {
        this.roleCheckService = roleCheckService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();

            // Read request body as JSON String
            String requestBody = new String(wrappedRequest.getInputStream().readAllBytes());

            // Collect headers in a Map
            Map<String, String> headersMap = new HashMap<>();
            Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headersMap.put(headerName, wrappedRequest.getHeader(headerName));
            }

            // Convert headers and body to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestPayload = objectMapper.writeValueAsString(Map.of(
                    "headers", headersMap,
                    "body", requestBody
            ));

            // Pass the JSON payload to the access check service
            boolean hasAccess = roleCheckService.hasAccess(username, request.getRequestURI(), requestPayload);
            if (!hasAccess) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        filterChain.doFilter(wrappedRequest, response);
    }
}

