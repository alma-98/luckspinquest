package com.luckspinquest.repository;

import com.luckspinquest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCode(String roleCode);

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleCode(String roleCode);

    boolean existsByRoleName(String roleName);
}
