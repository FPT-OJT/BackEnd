package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.FavoriteMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteMerchantRepository
        extends JpaRepository<FavoriteMerchant, UUID>, JpaSpecificationExecutor<FavoriteMerchant> {
    boolean existsByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);

    @Query("""
                SELECT DISTINCT m
                FROM FavoriteMerchant fm
                JOIN fm.merchantAgency ma
                JOIN ma.merchant m
                WHERE fm.user.id = :userId
            """)
    List<Merchant> findDistinctFavoriteMerchantsByUserId(UUID userId);

    @Modifying
    @Query("""
                UPDATE FavoriteMerchant fm
                SET fm.deletedAt = CURRENT_TIMESTAMP
                WHERE fm.id = :favoriteMerchantId
                  AND fm.user.id = :userId
                  AND fm.deletedAt IS NULL
            """)
    int deleteByUserIdAndId(UUID userId, UUID favoriteMerchantId);

    @Modifying
    @Query(value = """
                WITH restored AS (
                    UPDATE favorite_merchants
                    SET deleted_at = NULL,
                        updated_at = now()
                    WHERE user_id = :userId
                      AND merchant_agency_id = :merchantAgencyId
                      AND deleted_at IS NOT NULL
                    RETURNING id
                )
                INSERT INTO favorite_merchants (id, user_id, merchant_agency_id, created_at)
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
