package com.fpt.ojt.models.postgres.merchant;

public interface NearestAgencyProjection {

    String getBrandName();

    String getAgencyName();

    Double getLatitude();

    Double getLongitude();

    String getLogoUrl();

    String getDescription();

    Double getDistanceMeters();
}
