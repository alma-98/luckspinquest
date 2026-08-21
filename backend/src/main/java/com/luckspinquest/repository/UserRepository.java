package com.luckspinquest.repository;

import com.luckspinquest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserUsername(String username);

    Optional<User> findByUserEmail(String email);

    Optional<User> findByUserPhone(String phone);

    boolean existsByUserUsername(String username);

    boolean existsByUserEmail(String email);

    boolean existsByUserPhone(String phone);
}
