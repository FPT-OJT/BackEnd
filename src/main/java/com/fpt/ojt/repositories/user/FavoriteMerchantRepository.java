package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.user.FavoriteMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FavoriteMerchantRepository extends JpaRepository<FavoriteMerchant, UUID>, JpaSpecificationExecutor<FavoriteMerchant> {
    boolean existsByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);
}
