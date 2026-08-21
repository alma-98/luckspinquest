package com.luckspinquest.repository;

import com.luckspinquest.entity.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserUserId(Long userId);

    @Query("""
        SELECT ur
        FROM UserRole ur
        JOIN FETCH ur.role r
        WHERE ur.user.userId = :userId
    """)
    List<UserRole> findByUserUserIdWithRole(
            @Param("userId") Long userId
    );

    List<UserRole> findByRoleRoleId(Long roleId);

    Optional<UserRole> findByUserUserIdAndRoleRoleId(
            Long userId,
            Long roleId
    );

    boolean existsByUserUserIdAndRoleRoleId(
            Long userId,
            Long roleId
    );
}
