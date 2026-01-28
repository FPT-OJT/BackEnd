package com.fpt.ojt.models.postgres.card;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_products")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CardProduct extends AbstractBaseEntity {
    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "card_code", nullable = false, unique = true)
    private String cardCode;
}
