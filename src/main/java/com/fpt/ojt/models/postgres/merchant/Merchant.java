package com.fpt.ojt.models.postgres.merchant;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "merchants")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Merchant extends AbstractBaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mcc", nullable = false, unique = true)
    private String mcc;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MerchantCategory category;

    @Column(name = "logo_url")
    private String logoUrl;
}
