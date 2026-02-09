package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.user.SubscribedMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SubscribedMerchantRepository extends JpaRepository<SubscribedMerchant, UUID>, JpaSpecificationExecutor<SubscribedMerchant> {
    boolean existsByUserIdAndMerchantAgencyId(UUID userId, UUID merchantAgencyId);
}
