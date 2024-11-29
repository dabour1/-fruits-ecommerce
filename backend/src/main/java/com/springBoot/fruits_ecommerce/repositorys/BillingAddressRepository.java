package com.springBoot.fruits_ecommerce.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.BillingAddress;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {

}
