package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.CardRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRuleRepository extends JpaRepository<CardRule, UUID>, JpaSpecificationExecutor<CardRule> {

    @Query(value = """
            SELECT cr.*
            FROM card_rules cr
            INNER JOIN card_products cp ON cr.card_product_id = cp.id
            INNER JOIN user_credit_cards ucc ON cp.id = ucc.card_product_id
            WHERE ucc.user_id = :userId
              AND ucc.expiry_date > CURRENT_DATE
              AND cr.deleted_at IS NULL
              AND cp.deleted_at IS NULL
              AND ucc.deleted_at IS NULL
              AND cr.match_conditions IS NULL
            ORDER BY cr.created_at DESC
            """, nativeQuery = true)
    List<CardRule> findAllAvailableByCardRulesByUserId(@Param("userId") UUID userId);
}
