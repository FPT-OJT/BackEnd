package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.FavoriteMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    void deleteByUserIdAndId(UUID userId, UUID favoriteMerchantId);

}
