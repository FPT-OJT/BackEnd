package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.UserCreditCard;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserCreditCardRepository extends JpaRepository<UserCreditCard, UUID>, JpaSpecificationExecutor<UserCreditCard> {

    @EntityGraph(attributePaths = { "user", "cardProduct" })
    List<UserCreditCard> findByUserIdAndDeletedAtIsNull(UUID userId);

    @EntityGraph(attributePaths = { "user", "cardProduct" })
    Optional<UserCreditCard> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);

    @Query("""
                SELECT ucc
                FROM UserCreditCard ucc
                JOIN FETCH ucc.cardProduct cp
                WHERE ucc.user.id = :userId
                  AND cp.cardType = :cardType
            """)
    List<UserCreditCard> findByUserIdAndCardType(UUID userId, String cardType);
    boolean existsByUserIdAndDeletedAtIsNull(UUID userId);

}
