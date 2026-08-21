package com.luckspinquest.repository;

import com.luckspinquest.entity.CoinPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoinPackageRepository
        extends JpaRepository<CoinPackage, Long> {

    Optional<CoinPackage> findByPackageCode(String packageCode);

    List<CoinPackage> findByPackageStatusOrderBySortOrderAsc(
        String packageStatus
    );

    boolean existsByPackageCode(String packageCode);
}
