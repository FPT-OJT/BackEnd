package com.fpt.ojt.repositories.merchant;

import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantAgencyRepository extends JpaRepository<MerchantAgency, UUID>, JpaSpecificationExecutor<MerchantAgency> {

    /**
     * Find merchant agencies by MCC with eager loading of merchant
     */
    @EntityGraph(attributePaths = {"merchant"})
    @Query("""
            SELECT DISTINCT ma FROM MerchantAgency ma
            WHERE ma.merchant.mcc = :mcc
              AND ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
            """)
    List<MerchantAgency> findByMerchantMcc(@Param("mcc") String mcc);

    /**
     * Find all non-deleted merchant agencies with eager loading of merchant
     */
    @EntityGraph(attributePaths = {"merchant"})
    @Query("""
            SELECT ma FROM MerchantAgency ma
            WHERE ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
            """)
    List<MerchantAgency> findAllActiveWithMerchant();

    /**
     * Find all non-deleted merchant agencies excluding specific MCCs with eager loading
     */
    @EntityGraph(attributePaths = {"merchant"})
    @Query("""
            SELECT ma FROM MerchantAgency ma
            WHERE ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
              AND ma.merchant.mcc NOT IN :excludedMccs
            """)
    List<MerchantAgency> findAllActiveExcludingMccs(@Param("excludedMccs") List<String> excludedMccs);
}
