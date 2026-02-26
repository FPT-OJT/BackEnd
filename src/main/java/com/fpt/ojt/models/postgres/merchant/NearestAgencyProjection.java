package com.fpt.ojt.models.postgres.merchant;

public interface NearestAgencyProjection {
    String getAgencyId();

    String getMerchantName();

    String getAgencyName();

    Double getLatitude();

    Double getLongitude();

    String getLogoUrl();

    String getDescription();

    Double getDistanceMeters();
}
