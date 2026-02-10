package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.SubscribedMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribedMerchantRepository
        extends JpaRepository<SubscribedMerchant, UUID>, JpaSpecificationExecutor<SubscribedMerchant> {
    boolean existsByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);

    @Query("""
                SELECT DISTINCT m
                FROM SubscribedMerchant sm
                JOIN sm.merchantAgency ma
                JOIN ma.merchant m
                WHERE sm.user.id = :userId
                  AND sm.deletedAt IS NULL
            """)
    List<Merchant> findDistinctSubscribedMerchantsByUserId(UUID userId);

    @Query("""
                SELECT DISTINCT ma
                FROM SubscribedMerchant sm
                JOIN sm.merchantAgency ma
                WHERE sm.user.id = :userId
                  AND sm.deletedAt IS NULL
            """)
    List<MerchantAgency> findDistinctSubscribedMerchantAgenciesByUserId(UUID userId);

    Optional<SubscribedMerchant> findByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);

    @Query("""
                select sm.merchantAgency.id
                from SubscribedMerchant sm
                where sm.user.id = :userId
                  and sm.merchantAgency.merchant.id = :merchantId
                  and sm.deletedAt IS NULL
            """)
    List<UUID> findMerchantAgencyIdsByUserIdAndMerchantId(UUID userId, UUID merchantId);

    @Modifying
    @Query("""
                UPDATE SubscribedMerchant sm
                SET sm.deletedAt = CURRENT_TIMESTAMP
                WHERE sm.user.id = :userId
                  AND sm.merchantAgency.merchant.id = :merchantId
                  AND sm.deletedAt IS NULL
            """)
    int deleteByUserIdAndMerchantId(UUID userId, UUID merchantId);

    @Modifying
    @Query("""
                UPDATE SubscribedMerchant sm
                SET sm.deletedAt = CURRENT_TIMESTAMP
                WHERE sm.user.id = :userId
                  AND sm.merchantAgency.id = :merchantAgencyId
                  AND sm.deletedAt IS NULL
            """)
    int deleteByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);

    @Modifying
    @Query(value = """
                WITH restored AS (
                    UPDATE subscribed_merchant
                    SET deleted_at = NULL,
                        updated_at = now()
                    WHERE user_id = :userId
                      AND merchant_agency_id = :merchantAgencyId
                      AND deleted_at IS NOT NULL
                    RETURNING id
                )
                INSERT INTO subscribed_merchant (id, user_id, merchant_agency_id, created_at)
                SELECT gen_random_uuid(), :userId, :merchantAgencyId, now()
                WHERE NOT EXISTS (SELECT 1 FROM restored)
                ON CONFLICT (user_id, merchant_agency_id)
                WHERE deleted_at IS NULL
                DO NOTHING
            """, nativeQuery = true)
    int insertOrRestore(
            @Param("userId") UUID userId,
            @Param("merchantAgencyId") UUID merchantAgencyId);
}
