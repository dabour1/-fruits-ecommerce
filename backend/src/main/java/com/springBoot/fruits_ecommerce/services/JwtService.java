package com.springBoot.fruits_ecommerce.services;
 
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String secret="12df32e69496a167c9d6196abec4bd4a25858e8da5fa4fe150ac9e03431ca36b";
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
        return Jwts.parser().verifyWith( getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
     public String generateToken(User user) {
    
        return createToken( user,new HashMap<>() );
    }
     public String generateToken(User user,Map<String, Object> extraClaims) {
    
        return createToken( user,extraClaims);
    }
 
     private String createToken( User user, Map<String, Object> extraClaims) {
        return Jwts.builder().claims(extraClaims).subject(user.getEmail()).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith( getSigningKey()).compact();
    }
     public SecretKey getSigningKey( ) {
        byte [] keyBytes=Decoders.BASE64URL.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);

       
    }

    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractEmail(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
