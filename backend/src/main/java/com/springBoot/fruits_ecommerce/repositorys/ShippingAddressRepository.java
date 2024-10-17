package com.springBoot.fruits_ecommerce.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.ShippingAddress;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

}
