package com.karhacter.movies_webapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import com.karhacter.movies_webapp.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {
    private final String SECRET = "ouuiS5p6BGpIAnS+H3lgJAquyp+Smx7eVdWJXKIQIUM=";
    private final long jwtExpirationMs = 86400000; // 1 day

    private final Key key = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private static Set<String> invalidatedTokens = new HashSet<>();

    public boolean validateToken(String token) {
        try {
            if (invalidatedTokens.contains(token)) {
                System.err.println("Token was invalidated");
                return false;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token validation successful for subject: " +
                    claims.getSubject());
            System.out.println("Token expiration: " + claims.getExpiration());

            return !invalidatedTokens.contains(token);
        } catch (ExpiredJwtException ex) {
            System.err.println("Token expired: " + ex.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
        System.out.println("Token invalidated: " + token);
    }
}
