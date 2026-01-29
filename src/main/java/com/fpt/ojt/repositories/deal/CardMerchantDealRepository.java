package com.fpt.ojt.repositories.deal;

import com.fpt.ojt.models.postgres.deal.CardMerchantDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardMerchantDealRepository extends JpaRepository<CardMerchantDeal, UUID>, JpaSpecificationExecutor<CardMerchantDeal> {
}
