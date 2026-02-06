package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.UserCreditCard;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserCreditCardRepository extends JpaRepository<UserCreditCard, UUID>, JpaSpecificationExecutor<UserCreditCard> {

    @EntityGraph(attributePaths = { "user", "cardProduct" })
    List<UserCreditCard> findByUserIdAndDeletedAtIsNull(UUID userId);

    boolean existsByUserIdAndDeletedAtIsNull(UUID userId);

}
