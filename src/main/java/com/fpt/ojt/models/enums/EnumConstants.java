package com.fpt.ojt.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class EnumConstants {

    @Getter
    @AllArgsConstructor
    public enum RoleEnum {
        ADMIN("ADMIN"),
        CUSTOMER("CUSTOMER");
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum ConditionTypeEnum {
        STRING("string"),
        NUMBER("number"),
        BOOLEAN("boolean");
        private final String value;
    }
}
