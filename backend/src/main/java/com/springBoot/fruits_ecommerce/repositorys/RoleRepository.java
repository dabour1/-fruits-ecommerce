package com.springBoot.fruits_ecommerce.repositorys;

import com.springBoot.fruits_ecommerce.enums.RoleName;
import com.springBoot.fruits_ecommerce.models.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

        Optional<Role> findByName(RoleName name);

}
