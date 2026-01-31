package com.fpt.ojt.repositories.deal;

import com.fpt.ojt.models.postgres.deal.CardMerchantDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardMerchantDealRepository extends JpaRepository<CardMerchantDeal, UUID>, JpaSpecificationExecutor<CardMerchantDeal> {
    @Query(value = """
            SELECT COUNT(*) = 0
            FROM card_merchant_deals
            WHERE merchant_deal_id = :dealId
              AND deleted_at IS NULL
            """, nativeQuery = true)
    boolean isDealAvailableForAllCards(@Param("dealId") UUID dealId);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM card_merchant_deals
            WHERE merchant_deal_id = :dealId
              AND card_product_id = :cardProductId
              AND deleted_at IS NULL
            """, nativeQuery = true)
    boolean existsByDealAndCard(@Param("dealId") UUID dealId, @Param("cardProductId") UUID cardProductId);
}
