package com.fpt.ojt.presentations.dtos.requests.user;

import java.util.UUID;

import lombok.Data;

@Data
public class AddFavoriteMerchantRequest {
    private UUID merchantAgencyId;
}
