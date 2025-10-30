package me.imsergioh.livecore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.imsergioh.livecore.config.MainConfig;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String secret = MainConfig.getSecret();

    private static final long expirationSecs = MainConfig.getExpirationSecs();

    private static final String issuer = MainConfig.getIssuer();

    private static Key key;

    public static void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public static String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (expirationSecs * 1000)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
