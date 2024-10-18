package com.springBoot.fruits_ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "billing_addresses")
public class BillingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "customer_related_information_id")
    private CustomerRelatedInformation customerRelatedInformation;
    @NotBlank(message = "Address Line One is required")
    @NotNull(message = "Address Line One cannot be null")
    private String addressLineOne;
    @NotBlank(message = "Address Line Two is required")
    private String addressLineTwo;
    @NotBlank(message = "Post Code is required")
    private String postCode;
}
