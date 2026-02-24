package com.fpt.ojt.models.postgres.merchant;

import java.util.UUID;

public interface GeofenceAgencyProjection {

    UUID getId();

    Double getLatitude();

    Double getLongitude();
}
