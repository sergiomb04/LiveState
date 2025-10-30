package me.imsergioh.livecore.service;

import io.jsonwebtoken.Claims;
import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.util.JwtUtil;

public class AuthService {

    public static boolean checkTokenHeader(String authHeader) {
        String tokenPrefix = MainConfig.getTokenPrefix();
        if (authHeader == null || !authHeader.startsWith(tokenPrefix + " ")) return false;

        String token = authHeader.substring(7);
        return JwtUtil.validateToken(token);
    }

    public static String getUserIdFromHeader(String authHeader) {
        String token = authHeader.substring(7);
        Claims claims = JwtUtil.getClaims(token);
        return claims.getSubject();
    }
}
