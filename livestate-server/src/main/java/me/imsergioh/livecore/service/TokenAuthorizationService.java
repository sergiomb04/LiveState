package me.imsergioh.livecore.service;

public class TokenAuthorizationService {

    // Devuelve true si el token es válido y puede acceder al recurso
    public static boolean canAccessUser(String token, String userId) {
        if (token == null) return false;

        // Lógica de ejemplo
        if ("admin-token".equals(token)) return true; // admin acceso global
        if ("user-token".equals(token)) return "Bob".equals(userId); // solo user 2
        return false;
    }
}
