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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
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
    @Mock
    private AuthenticationManager authenticationManager;
    
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

    @Test
    public void testRegister_EmailAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> authenticationService.register(user));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testRegister_DefaultRoleNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.CLIENT)).thenReturn(Optional.empty());
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> authenticationService.register(user));
        assertEquals("Default role not found", exception.getMessage());
        verify(userRepository, times(0)).save(user);
     }
     @Test
    public void testAuthenticat_Success()throws Exception {
         AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");
        
        AuthenticationResponse response = authenticationService.authenticat(request);
         
        assertEquals("token", response.getToken());
        verify(jwtService ).generateToken(user);
        
    }
    @Test
    public void testAuthenticat_IncorrectEmailOrPassword() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticat(request));
        
        assertEquals("Incorrect email or password", exception.getMessage());
        verify(authenticationManager ).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(0)).generateToken(user);
    }
    @Test
    public void testAuthenticat_UserNotFound() throws Exception {
         
        AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
         
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticat(request));
        
        assertEquals("User not found", exception.getMessage());
        verify(authenticationManager ).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(0)).generateToken(user);
    }
    



}
