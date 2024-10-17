package com.springBoot.fruits_ecommerce.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "Username is required")
    @NotNull(message = "Username cannot be null")
    private String username;

    @NotBlank(message = "Email is required")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 250, message = "Password must be at least 8 characters")
    private String password;

    @JsonProperty("customerRelatedInformation")
    @Valid
    @NotNull(message = "Customer Information cannot be null")
    private CustomerRelatedInformationRequest customerRelatedInformation;

    @Data
    public static class CustomerRelatedInformationRequest {
        @Valid
        @NotNull(message = "Billing Address cannot be null")
        @JsonProperty("billingAddress")
        private BillingAddressRequest billingAddress;

        @Valid
        @NotNull(message = "Shipping Address cannot be null")
        @JsonProperty("shippingAddress")

        private ShippingAddressRequest shippingAddress;

        @Valid
        @NotNull(message = "Credit Card Information cannot be null")
        @JsonProperty("creditCardInfo")
        private CreditCardInfoRequest creditCardInfo;
    }

    @Data
    public static class BillingAddressRequest {
        @NotBlank(message = "Address Line One is required")
        @NotNull(message = "Address Line One cannot be null")
        @JsonProperty("addressLineOne")
        private String addressLineOne;

        @NotBlank(message = "Address Line Two is required")
        @JsonProperty("addressLineTwo")
        private String addressLineTwo;

        @NotBlank(message = "Post Code is required")
        @JsonProperty("postCode")
        private String postCode;
    }

    @Data
    public static class ShippingAddressRequest {
        @NotBlank(message = "Address Line One is required")
        @NotNull(message = "Address Line One cannot be null")
        @JsonProperty("addressLineOne")
        private String addressLineOne;

        @NotBlank(message = "Address Line Two is required")
        @JsonProperty("addressLineTwo")
        private String addressLineTwo;

        @NotBlank(message = "Post Code is required")
        @JsonProperty("postCode")
        private String postCode;
    }

    @Data
    public static class CreditCardInfoRequest {
        @NotBlank(message = "Card Number is required")
        @NotNull(message = "Card Number cannot be null")
        @JsonProperty("cardNumber")
        private String cardNumber;

        @NotBlank(message = "Expiration Date is required")
        @NotNull(message = "Expiration Date cannot be null")
        @JsonProperty("expirationDate")
        private String expirationDate;

        @NotBlank(message = "CVV is required")
        @NotNull(message = "CVV cannot be null")
        @JsonProperty("cvv")
        private String cvv;
    }
}
