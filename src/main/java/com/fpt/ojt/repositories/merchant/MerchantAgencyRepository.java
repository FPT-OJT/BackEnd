package com.fpt.ojt.repositories.merchant;

import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantAgencyRepository extends JpaRepository<MerchantAgency, UUID>, JpaSpecificationExecutor<MerchantAgency> {
}
