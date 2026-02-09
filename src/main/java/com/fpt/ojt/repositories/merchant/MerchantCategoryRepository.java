package com.fpt.ojt.repositories.merchant;

import com.fpt.ojt.models.postgres.merchant.MerchantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, UUID>, JpaSpecificationExecutor<MerchantCategory> {

    @Query("SELECT mc FROM MerchantCategory mc WHERE mc.deletedAt IS NULL")
    Page<MerchantCategory> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT mc FROM MerchantCategory mc WHERE mc.deletedAt IS NOT NULL")
    Page<MerchantCategory> findAllByDeletedAtIsNotNull(Pageable pageable);
}
