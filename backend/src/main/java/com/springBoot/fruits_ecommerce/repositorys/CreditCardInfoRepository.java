package com.springBoot.fruits_ecommerce.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.CreditCardInfo;

public interface CreditCardInfoRepository extends JpaRepository<CreditCardInfo, Long> {

}
