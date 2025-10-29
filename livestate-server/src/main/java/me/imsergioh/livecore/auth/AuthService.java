package me.imsergioh.livecore.auth;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private static final Set<String> ADMIN_TOKENS = Set.of(
            "admin123",
            "supersecret",
            "admin-token"
    );

    public static boolean checkTokenHeader(String authHeader) {
        if (authHeader == null) return false;
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return isValidAdminToken(token);
        }
        return false;
    }

    public static boolean isValidAdminToken(String token) {
        return token != null && ADMIN_TOKENS.contains(token);
    }
}
