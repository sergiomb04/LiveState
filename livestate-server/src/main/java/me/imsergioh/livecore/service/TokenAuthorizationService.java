package me.imsergioh.livecore.service;

import me.imsergioh.livecore.util.JwtUtil;

public class TokenAuthorizationService {

    public static boolean canAccessUser(String token, String userId) {
        var claims = JwtUtil.getClaims(token);
        String subject = claims.getSubject();

        String role = (String) claims.get("role");
        return subject.equals(userId) || "ADMIN".equalsIgnoreCase(role);
    }
}
