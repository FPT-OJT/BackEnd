package com.fpt.ojt.services.home;

import com.fpt.ojt.services.dtos.Coordinate;

public interface LocationService {
    Coordinate mapFromIpAddress(String ipAddress);
}
