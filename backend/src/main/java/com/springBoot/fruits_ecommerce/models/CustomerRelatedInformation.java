package com.springBoot.fruits_ecommerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customer_related_informations")
public class CustomerRelatedInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "User ID cannot be null")
    @NotBlank(message = "User ID is required")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(mappedBy = "customerRelatedInformation")

    private ShippingAddress shippingAddress;
    @NotNull(message = "Billing Address cannot be null")
    @NotBlank(message = "Billing Address is required")
    @OneToOne(mappedBy = "customerRelatedInformation")

    private BillingAddress billingAddress;

    @OneToOne(mappedBy = "customerRelatedInformation")
    private CreditCardInfo creditCardInfo;

}
