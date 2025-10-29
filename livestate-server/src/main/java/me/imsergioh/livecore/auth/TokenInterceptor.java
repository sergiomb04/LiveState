package me.imsergioh.livecore.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.imsergioh.livecore.instance.handler.ILiveStateHandler;
import me.imsergioh.livecore.service.TokenAuthorizationService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println("PREHANDLER");
        // LiveStateHandler
        if (handler instanceof HandlerMethod handlerMethod) {
            Object bean = handlerMethod.getBean();
            Class<?> beanClass = bean.getClass();

            if (ILiveStateHandler.class.isAssignableFrom(beanClass)) {
                ILiveStateHandler<?> liveStateHandler = (ILiveStateHandler<?>) bean;

                // Si no tiene token auth -> pasa
                if (!liveStateHandler.hasTokenAuth()) return true;
            }
        }

        String token = getAuthToken(request);
        System.out.println("Parsing AUTHTOKEN " + token);

        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String userId = getPathVar(pathVars, "userId");

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        if (!TokenAuthorizationService.canAccessUser(token, userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return false;
        }
        return true;
    }

    private String getPathVar(Map<String, String> pathVars, String key) {
        return (pathVars != null) ? pathVars.get(key) : null;
    }

    private String getAuthToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            token = request.getParameter("token");
        }
        return token;
    }

}
