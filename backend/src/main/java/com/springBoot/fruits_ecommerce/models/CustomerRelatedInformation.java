package com.springBoot.fruits_ecommerce.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(mappedBy = "customerRelatedInformation")
    @JsonManagedReference
    private ShippingAddress shippingAddress;

    @OneToOne(mappedBy = "customerRelatedInformation")
    @JsonManagedReference
    private BillingAddress billingAddress;

    @OneToOne(mappedBy = "customerRelatedInformation")

    @JsonManagedReference
    private CreditCardInfo creditCardInfo;

}
