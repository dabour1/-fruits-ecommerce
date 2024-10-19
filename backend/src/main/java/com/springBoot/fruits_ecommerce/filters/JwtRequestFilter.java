package com.springBoot.fruits_ecommerce.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springBoot.fruits_ecommerce.services.JwtService;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        if (isExcludedRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromRequest(request);
        String email = (jwt != null) ? jwtService.extractEmail(jwt) : null;

        if (email != null && isNotAuthenticated()) {

            authenticateUser(request, jwt, email);
        }

        chain.doFilter(request, response);
    }

    private boolean isExcludedRequest(HttpServletRequest request) {
        return request.getServletPath().contains("/api/auth") || request.getServletPath().contains("/images");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private boolean isNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticateUser(HttpServletRequest request, String jwt, String email) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

        if (jwtService.validateToken(jwt, email)) {
            List<GrantedAuthority> authorities = extractRolesFromJwt(jwt);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println(authorities);
        }
    }

    private List<GrantedAuthority> extractRolesFromJwt(String jwt) {
        Claims claims = jwtService.extractAllClaims(jwt);
        List<String> roles = (List<String>) claims.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

}
