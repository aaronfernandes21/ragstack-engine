package com.aaron.aiDoc.util;

import com.aaron.aiDoc.entity.Role;
import com.aaron.aiDoc.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    UserRepo userRepo;

    public String generateToken(String username, String email, UUID userId, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("id", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }


    public String extractUserId(String token) {
        return extractAllClaims(token).get("id", String.class);
    }
}
