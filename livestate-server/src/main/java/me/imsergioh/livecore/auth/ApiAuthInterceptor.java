package me.imsergioh.livecore.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.imsergioh.livecore.instance.handler.ILiveStateHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // LiveStateHandler
        if (handler instanceof HandlerMethod handlerMethod) {
            Object bean = handlerMethod.getBean(); // Obtener el bean real
            Class<?> beanClass = bean.getClass();

            if (ILiveStateHandler.class.isAssignableFrom(beanClass)) {
                ILiveStateHandler<?> liveStateHandler = (ILiveStateHandler<?>) bean;

                // Si no tiene token auth -> pasa
                if (!liveStateHandler.hasTokenAuth()) return true;

                // Si tiene token auth -> check con AuthService
                return check(request, response);
            }
        }

        // Conventional /api/** Controller -> Check Auth header with AuthService
        return check(request, response);
    }

    public static boolean check(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (!AuthService.checkTokenHeader(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

}
