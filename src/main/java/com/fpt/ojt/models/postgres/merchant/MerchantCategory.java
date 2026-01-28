package com.fpt.ojt.models.postgres.merchant;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "merchant_categories")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCategory extends AbstractBaseEntity {
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "image_url")
    private String imageUrl;
}
