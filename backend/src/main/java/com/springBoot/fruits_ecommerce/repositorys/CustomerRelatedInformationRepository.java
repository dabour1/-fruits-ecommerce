package com.springBoot.fruits_ecommerce.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.CustomerRelatedInformation;

public interface CustomerRelatedInformationRepository extends JpaRepository<CustomerRelatedInformation, Long> {

}
