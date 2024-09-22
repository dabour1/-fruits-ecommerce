package com.springBoot.fruits_ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
 
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springBoot.fruits_ecommerce.filters.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private UserDetailsService userDetailsServiceI;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http.csrf(AbstractHttpConfigurer::disable) 
               .authorizeHttpRequests(
                req->req.requestMatchers("/api/auth/**")
                .permitAll()
               .anyRequest().authenticated()
               ).
               userDetailsService(userDetailsServiceI)
               .sessionManagement(session->session
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
   }
}
