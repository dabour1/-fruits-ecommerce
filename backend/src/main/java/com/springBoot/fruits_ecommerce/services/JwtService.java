package com.springBoot.fruits_ecommerce.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.models.Role;
import com.springBoot.fruits_ecommerce.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;
    @Value("${jwt.secret}")
    private String SECRET;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(User user) {

        Map<String, Object> extraClaims = addExtraClaims(user);

        return createToken(user, extraClaims);
    }

    private Map<String, Object> addExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return extraClaims;

    }

    public String generateToken(User user, Map<String, Object> extraClaims) {

        return createToken(user, extraClaims);
    }

    private String createToken(User user, Map<String, Object> extraClaims) {

        return Jwts.builder().claims(extraClaims).subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey()).compact();
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractEmail(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
