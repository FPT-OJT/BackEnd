package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.UserCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCreditCardRepository extends JpaRepository<UserCreditCard, UUID>, JpaSpecificationExecutor<UserCreditCard> {
}
