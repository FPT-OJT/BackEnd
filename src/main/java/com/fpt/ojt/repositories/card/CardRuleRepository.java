package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.CardRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRuleRepository extends JpaRepository<CardRule, UUID>, JpaSpecificationExecutor<CardRule> {
}
