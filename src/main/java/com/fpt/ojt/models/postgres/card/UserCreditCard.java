package com.fpt.ojt.models.postgres.card;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import com.fpt.ojt.models.postgres.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_credit_cards")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreditCard extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_id", nullable = false)
    private CardProduct cardProduct;

    @Column(name = "first_payment_date")
    private LocalDate firstPaymentDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
