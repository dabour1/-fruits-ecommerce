package com.springBoot.fruits_ecommerce.config;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
 

import com.springBoot.fruits_ecommerce.repositorys.UserRepository;

@Configuration
 
public class ApplicationConfig {

  @Autowired
  private UserRepository repository;
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> repository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
  

   
}
