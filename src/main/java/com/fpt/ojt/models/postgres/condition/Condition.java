package com.fpt.ojt.models.postgres.condition;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "conditions")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Condition extends AbstractBaseEntity {
    @Column(name = "type")
    private String type;

    @Column(name = "label")
    private String label;

    @Column(name = "condition_code", unique = true)
    private String conditionCode;
}
