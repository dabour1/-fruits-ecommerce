package com.springBoot.fruits_ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "credit_card_infos")
public class CreditCardInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "customer_related_information_id")
    private CustomerRelatedInformation customerRelatedInformation;

    @NotNull(message = "Card Number cannot be null")
    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotNull(message = "Expiration Date cannot be null")
    @NotBlank(message = "Expiration Date is required")
    private String expirationDate;

    @NotNull(message = "CVV cannot be null")
    @NotBlank(message = "CVV is required")
    private String cvv;

}
