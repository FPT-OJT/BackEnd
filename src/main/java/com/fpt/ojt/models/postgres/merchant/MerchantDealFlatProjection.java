package com.fpt.ojt.models.postgres.merchant;

import java.time.LocalDate;
import java.util.UUID;

public interface MerchantDealFlatProjection {
    UUID getAgencyId();
    String getAgencyName();
    Double getLatitude();
    Double getLongitude();
    
    String getMerchantMcc();
    String getMerchantLogoUrl();
    
    UUID getDealId();
    String getDealName();
    Double getDiscountRate();
    Double getCashbackRate();
    Double getPointsMultiplier();
    String getDescription();
    LocalDate getValidFrom();
    LocalDate getValidTo();
    
    Double getDistanceMeters();
}