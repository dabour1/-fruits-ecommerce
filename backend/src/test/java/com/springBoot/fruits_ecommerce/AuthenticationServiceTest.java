package com.springBoot.fruits_ecommerce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.Role;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.repositorys.RoleRepository;
import com.springBoot.fruits_ecommerce.repositorys.UserRepository;
import com.springBoot.fruits_ecommerce.services.AuthenticationService;
import com.springBoot.fruits_ecommerce.services.JwtService;
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    
    @InjectMocks
    private AuthenticationService authenticationService;

    private  Role clientRole;
    private User user;
 
    
    @BeforeEach
    void setUp() {
         clientRole = new Role(1L,RoleName.CLIENT);
         user = new User();
         user.setId(1L);  
         user.setUsername("Dabour");
         user.setEmail("dabour8@gmail.com");
     
    }
 
    @Test 
    public void testRegister_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.CLIENT)).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken"); 
      
        AuthenticationResponse response = authenticationService.register(user);
        
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains(clientRole));
        verify(userRepository ).save(user);
    }
    @Test
    public void testRegister_UserNameAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> authenticationService.register(user));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

   

}
