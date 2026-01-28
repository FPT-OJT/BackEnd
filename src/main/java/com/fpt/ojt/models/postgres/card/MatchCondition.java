package com.fpt.ojt.models.postgres.card;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchCondition {
    private String conditionCode;
    private List<Object> acceptedValues;
    private List<Object> rejectedValues;
}
