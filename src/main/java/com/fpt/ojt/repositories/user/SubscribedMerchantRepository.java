package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.SubscribedMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
            """)
    List<Merchant> findDistinctSubscribedMerchantsByUserId(UUID userId);

    @Query("""
                SELECT DISTINCT ma
                FROM SubscribedMerchant sm
                JOIN sm.merchantAgency ma
                WHERE sm.user.id = :userId
            """)
    List<MerchantAgency> findDistinctSubscribedMerchantAgenciesByUserId(UUID userId);

    Optional<SubscribedMerchant> findByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);

    @Query("""
                select sm.merchantAgency.id
                from SubscribedMerchant sm
                where sm.user.id = :userId
                  and sm.merchantAgency.merchant.id = :merchantId
            """)
    List<UUID> findMerchantAgencyIdsByUserIdAndMerchantId(UUID userId, UUID merchantId);

    @Modifying
    @Query("""
                delete from SubscribedMerchant sm
                where sm.user.id = :userId
                  and sm.merchantAgency.merchant.id = :merchantId
            """)
    void deleteByUserIdAndMerchantId(UUID userId, UUID merchantId);

}
