package me.imsergioh.livecore.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.service.AuthService;
import me.imsergioh.livecore.service.TokenAuthorizationService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class UnifiedApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Validar token base
        String authHeader = request.getHeader(MainConfig.getAuthHeader());
        if (!AuthService.checkTokenHeader(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Validación extra solo para rutas /api/user/**
        String path = request.getRequestURI();
        if (path.startsWith("/api/user/")) {
            String token = extractToken(authHeader, request);

            @SuppressWarnings("unchecked")
            Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String userId = (pathVars != null) ? pathVars.get("userId") : null;

            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }

            if (!TokenAuthorizationService.canAccessUser(token, userId)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }

        return true;
    }

    private String extractToken(String authHeader, HttpServletRequest request) {
        String tokenPrefix = MainConfig.getTokenPrefix();
        if (authHeader != null && authHeader.startsWith(tokenPrefix + " ")) {
            return authHeader.substring(7);
        }
        return request.getParameter("token");
    }
}
