package com.fpt.ojt.models.postgres.condition;

import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.AbstractBaseEntity;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private EnumConstants.ConditionTypeEnum type;

    @Column(name = "label")
    private String label;

    @Column(name = "condition_code", unique = true)
    private String conditionCode;
}
