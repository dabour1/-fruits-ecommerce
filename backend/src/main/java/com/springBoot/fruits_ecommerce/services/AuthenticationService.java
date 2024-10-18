package com.springBoot.fruits_ecommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.AuthenticationRequest;
import com.springBoot.fruits_ecommerce.models.AuthenticationResponse;
import com.springBoot.fruits_ecommerce.models.BillingAddress;
import com.springBoot.fruits_ecommerce.models.CreditCardInfo;
import com.springBoot.fruits_ecommerce.models.CustomerRelatedInformation;
import com.springBoot.fruits_ecommerce.models.RegistrationRequest; 
import com.springBoot.fruits_ecommerce.models.ShippingAddress;
import com.springBoot.fruits_ecommerce.models.User;
import com.springBoot.fruits_ecommerce.repositorys.BillingAddressRepository;
import com.springBoot.fruits_ecommerce.repositorys.CreditCardInfoRepository;
import com.springBoot.fruits_ecommerce.repositorys.CustomerRelatedInformationRepository;
import com.springBoot.fruits_ecommerce.repositorys.RoleRepository;
import com.springBoot.fruits_ecommerce.repositorys.ShippingAddressRepository;
import com.springBoot.fruits_ecommerce.repositorys.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRelatedInformationRepository customerRelatedInformationRepository;
    @Autowired
    private BillingAddressRepository billingAddressRepository;
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private CreditCardInfoRepository creditCardInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        Optional<User> userNameExisting = userRepository.findByUsername(registrationRequest.getUsername());
        if (userNameExisting.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Optional<User> emailExisting = userRepository.findByEmail(registrationRequest.getEmail());
        if (emailExisting.isPresent()) {

            throw new IllegalArgumentException("Email already exists");
        }
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        var defaultRole = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        User RequestedUser = createNewUser(registrationRequest);

        RequestedUser.getRoles().add(defaultRole);
        User user = userRepository.save(RequestedUser);
        CustomerRelatedInformation customerRelatedInformation = createNewCustomerRelatedInformation(user);
        customerRelatedInformationRepository.save(customerRelatedInformation);
        BillingAddress billingAddress = createBillingAddress(registrationRequest,
                customerRelatedInformation);
        billingAddressRepository.save(billingAddress);
        ShippingAddress shippingAddress = createShippingAddressRequest(registrationRequest,
                customerRelatedInformation);
        shippingAddressRepository.save(shippingAddress);
        CreditCardInfo creditCardInfo = createCreditCardInfo(registrationRequest,
                customerRelatedInformation);
        creditCardInfoRepository.save(creditCardInfo);

        final String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);

    }

    private User createNewUser(RegistrationRequest registrationRequest) {

        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        user.setUsername(registrationRequest.getUsername());
        return user;

    }

    private CustomerRelatedInformation createNewCustomerRelatedInformation(
            User user) {
        CustomerRelatedInformation customerRelatedInformation = new CustomerRelatedInformation();
        customerRelatedInformation.setUser(user);
        return customerRelatedInformation;
    }

    private BillingAddress createBillingAddress(RegistrationRequest registrationRequest,
            CustomerRelatedInformation customerRelatedInformation) {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCustomerRelatedInformation(customerRelatedInformation);
        billingAddress.setAddressLineOne(
                registrationRequest.getCustomerRelatedInformation().getBillingAddress().getAddressLineOne());
        billingAddress.setAddressLineTwo(
                registrationRequest.getCustomerRelatedInformation().getBillingAddress().getAddressLineTwo());
        billingAddress
                .setPostCode(registrationRequest.getCustomerRelatedInformation().getBillingAddress().getPostCode());
        return billingAddress;
    }

    private ShippingAddress createShippingAddressRequest(RegistrationRequest registrationRequest,
            CustomerRelatedInformation customerRelatedInformation) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCustomerRelatedInformation(customerRelatedInformation);
        shippingAddress.setAddressLineOne(
                registrationRequest.getCustomerRelatedInformation().getShippingAddress().getAddressLineOne());
        shippingAddress.setAddressLineTwo(
                registrationRequest.getCustomerRelatedInformation().getShippingAddress().getAddressLineTwo());
        shippingAddress
                .setPostCode(registrationRequest.getCustomerRelatedInformation().getShippingAddress().getPostCode());
        return shippingAddress;
    }

    private CreditCardInfo createCreditCardInfo(RegistrationRequest registrationRequest,
            CustomerRelatedInformation customerRelatedInformation) {
        CreditCardInfo creditCardInfo = new CreditCardInfo();
        creditCardInfo.setCustomerRelatedInformation(customerRelatedInformation);
        creditCardInfo
                .setCardNumber(registrationRequest.getCustomerRelatedInformation().getCreditCardInfo().getCardNumber());

        creditCardInfo.setExpirationDate(
                registrationRequest.getCustomerRelatedInformation().getCreditCardInfo().getExpirationDate());
        creditCardInfo.setCvv(registrationRequest.getCustomerRelatedInformation().getCreditCardInfo().getCvv());
        return creditCardInfo;
    }

    public AuthenticationResponse authenticat(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid credentials", e);
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ;
        final String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

}
