package com.fpt.ojt.repositories.deal;

import com.fpt.ojt.models.postgres.deal.MerchantDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantDealRepository extends JpaRepository<MerchantDeal, UUID>, JpaSpecificationExecutor<MerchantDeal> {
}
