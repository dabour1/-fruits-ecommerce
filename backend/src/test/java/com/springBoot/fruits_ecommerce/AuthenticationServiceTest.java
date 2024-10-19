package com.springBoot.fruits_ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.BillingAddress;
import com.springBoot.fruits_ecommerce.models.CreditCardInfo;
import com.springBoot.fruits_ecommerce.models.CustomerRelatedInformation;
import com.springBoot.fruits_ecommerce.models.RegistrationRequest;
import com.springBoot.fruits_ecommerce.models.Role;
import com.springBoot.fruits_ecommerce.models.ShippingAddress;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.repositorys.BillingAddressRepository;
import com.springBoot.fruits_ecommerce.repositorys.CreditCardInfoRepository;
import com.springBoot.fruits_ecommerce.repositorys.CustomerRelatedInformationRepository;
import com.springBoot.fruits_ecommerce.repositorys.RoleRepository;
import com.springBoot.fruits_ecommerce.repositorys.ShippingAddressRepository;
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
    private CustomerRelatedInformationRepository customerRelatedInformationRepository;

    @Mock
    private BillingAddressRepository billingAddressRepository;

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private CreditCardInfoRepository creditCardInfoRepository;

    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Role clientRole;
    private User user;
    private RegistrationRequest registrationRequest;
    private CreditCardInfo creditCardInfo;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    CustomerRelatedInformation customerRelatedInformation;

    @BeforeEach
    void setUp() {
        clientRole = Role.createRole(RoleName.CLIENT);
        user = new User();
        user.setId(1L);
        user.setUsername("Dabour");
        user.setEmail("dabour8@gmail.com");

        RegistrationRequest.BillingAddressRequest billingAddress = new RegistrationRequest.BillingAddressRequest();
        billingAddress.setAddressLineOne("123 Main Street");
        billingAddress.setAddressLineTwo("Apartment 4B");
        billingAddress.setPostCode("12345");

        RegistrationRequest.ShippingAddressRequest shippingAddress = new RegistrationRequest.ShippingAddressRequest();
        shippingAddress.setAddressLineOne("456 Secondary Street");
        shippingAddress.setAddressLineTwo("Suite 10");
        shippingAddress.setPostCode("67890");

        RegistrationRequest.CreditCardInfoRequest creditCardInfo = new RegistrationRequest.CreditCardInfoRequest();
        creditCardInfo.setCardNumber("4111111111111111");
        creditCardInfo.setExpirationDate("12/25");
        creditCardInfo.setCvv("123");

        RegistrationRequest.CustomerRelatedInformationRequest customerRelatedInformation = new RegistrationRequest.CustomerRelatedInformationRequest();
        customerRelatedInformation.setBillingAddress(billingAddress);
        customerRelatedInformation.setShippingAddress(shippingAddress);
        customerRelatedInformation.setCreditCardInfo(creditCardInfo);

        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("john_doe");
        registrationRequest.setEmail("john.doe@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setCustomerRelatedInformation(customerRelatedInformation);

    }

    @Test
    public void testRegister_Success() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.CLIENT)).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(registrationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegister_UserNameAlreadyExists() {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(registrationRequest));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testRegister_EmailAlreadyExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(registrationRequest));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testRegister_DefaultRoleNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.CLIENT)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(registrationRequest));
        assertEquals("Default role not found", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testAuthenticat_Success() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = authenticationService.authenticat(request);

        assertEquals("token", response.getToken());
        verify(jwtService).generateToken(user);

    }

    @Test
    public void testAuthenticat_IncorrectEmailOrPassword() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticat(request));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(0)).generateToken(user);
    }

    @Test
    public void testAuthenticat_UserNotFound() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest("dabour8@gmail.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticat(request));

        assertEquals("User not found", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(0)).generateToken(user);
    }

}
