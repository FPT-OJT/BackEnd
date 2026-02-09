package com.fpt.ojt.models.postgres.card;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MatchCondition {
    private String conditionCode;
    private List<String> acceptedValues;
    private List<String> rejectedValues;
}
