package com.fpt.ojt.services.merchants.impl;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component("nearestMerchantCacheKeyGenerator")
public class NearestMerchantCacheKeyGenerator implements KeyGenerator {

    private static final double GRID_SIZE_METERS = 500; // 500m grid size
    private static final double LAT_METER = 111_320.0; // 1 degree latitude in meters

    public long latGrid(double lat) {
        return Math.round(lat * LAT_METER / GRID_SIZE_METERS);
    }

    public long lngGrid(double lat, double lng) {
        double meterPerLng = LAT_METER * Math.cos(Math.toRadians(lat));
        return Math.round(lng * meterPerLng / GRID_SIZE_METERS);
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String keyword = (String) params[0];
        double lat = (double) params[1];
        double lng = (double) params[2];
        int limit = (int) params[3];

        return buildCacheKey(lat, lng, keyword, limit);
    }

    private String buildCacheKey(
            double lat,
            double lng,
            String keyword,
            int limit) {
        long latGrid = latGrid(lat);
        long lngGrid = lngGrid(lat, lng);

        String q = keyword == null
                ? ""
                : keyword.trim().toLowerCase();

        return String.format(
                "search:nearby:lat=%d:lng=%d:q=%s:limit=%d",
                latGrid,
                lngGrid,
                q,
                limit);
    }

}