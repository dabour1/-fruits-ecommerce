package com.springBoot.fruits_ecommerce.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springBoot.fruits_ecommerce.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}