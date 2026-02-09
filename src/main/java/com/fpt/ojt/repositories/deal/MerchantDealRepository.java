package com.fpt.ojt.repositories.deal;

import com.fpt.ojt.models.postgres.deal.MerchantDeal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantDealRepository extends JpaRepository<MerchantDeal, UUID>, JpaSpecificationExecutor<MerchantDeal> {

    @EntityGraph(attributePaths = {"merchantAgency", "merchantAgency.merchant"})
    @Query("""
            SELECT md FROM MerchantDeal md
            WHERE md.deletedAt IS NULL
              AND md.validFrom <= CURRENT_DATE
              AND md.validTo >= CURRENT_DATE
            """)
    List<MerchantDeal> findAllAvailableMerchantDeals();
}
