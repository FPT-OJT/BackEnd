package com.fpt.ojt.repositories.merchant;

import com.fpt.ojt.models.postgres.merchant.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, UUID>, JpaSpecificationExecutor<MerchantCategory> {
}
