package com.springBoot.fruits_ecommerce;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.services.JwtService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = "jwt.expiration=3600000")
public class JwtServiceTest {
    private JwtService jwtService;
    private SecretKey secretKey;
    private User user;

    @BeforeEach
    public void setUp() {
        jwtService = Mockito.spy(new JwtService());
        byte[] keyBytes = Decoders.BASE64.decode("12df32e69496a167c9d6196abec4bd4a25858e8da5fa4fe150ac9e03431ca36b");
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        doReturn(secretKey).when(jwtService).getSigningKey();
        user = new User();
        user.setEmail("user@example.com");
    }

    @Test
    public void testGenerateToken() {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", RoleName.CLIENT);

        String token = jwtService.generateToken(user, extraClaims);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testExtractEmail() {

        String token = jwtService.generateToken(user, new HashMap<>());
        String email = jwtService.extractEmail(token);
        assertEquals("user@example.com", email);
    }

    @Test
    public void testIsTokenExpired() {
        String token = jwtService.generateToken(user, new HashMap<>());
        Boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    public void testValidateToken() {
        String token = jwtService.generateToken(user, new HashMap<>());
        Boolean isValid = jwtService.validateToken(token, "user@example.com");
        assertTrue(isValid);
    }

}
