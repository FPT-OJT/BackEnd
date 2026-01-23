package com.fpt.ojt.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class Constants {
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    @Getter
    @AllArgsConstructor
    public enum RoleEnum {
        ADMIN("ADMIN"),
        CUSTOMER("CUSTOMER");
        private final String value;
    }
}
