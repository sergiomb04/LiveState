package me.imsergioh.livecore.auth;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private static final Set<String> ADMIN_TOKENS = Set.of(
            "admin123",
            "supersecret"
    );

    public static boolean isValidAdminToken(String token) {
        return token != null && ADMIN_TOKENS.contains(token);
    }
}
