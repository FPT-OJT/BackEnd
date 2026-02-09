package com.fpt.ojt.repositories.merchant;

import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.merchant.NearestAgencyProjection;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantAgencyRepository
        extends JpaRepository<MerchantAgency, UUID>, JpaSpecificationExecutor<MerchantAgency> {

    /**
     * Find merchant agencies by MCC with eager loading of merchant
     */
    @EntityGraph(attributePaths = { "merchant" })
    @Query("""
            SELECT DISTINCT ma FROM MerchantAgency ma
            WHERE ma.merchant.mcc = :mcc
              AND ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
            """)
    List<MerchantAgency> findByMerchantMcc(@Param("mcc") String mcc);

    /**
     * Find all non-deleted merchant agencies with eager loading of merchant
     */
    @EntityGraph(attributePaths = { "merchant" })
    @Query("""
            SELECT ma FROM MerchantAgency ma
            WHERE ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
            """)
    List<MerchantAgency> findAllActiveWithMerchant();

    /**
     * Find all non-deleted merchant agencies excluding specific MCCs with eager
     * loading
     */
    @EntityGraph(attributePaths = { "merchant" })
    @Query("""
            SELECT ma FROM MerchantAgency ma
            WHERE ma.deletedAt IS NULL
              AND ma.merchant.deletedAt IS NULL
              AND ma.merchant.mcc NOT IN :excludedMccs
            """)
    List<MerchantAgency> findAllActiveExcludingMccs(@Param("excludedMccs") List<String> excludedMccs);

    @Query(value = """
            WITH user_point AS (
                SELECT
                    ST_SetSRID(
                        ST_MakePoint(:userLng, :userLat),
                        4326 -- WGS 84
                    )::geography AS point
            )
            SELECT
                m.name AS brand_name,
                a.name AS agency_name,
                a.latitude,
                a.longitude,
                m.logo_url,
                m.description,
                ROUND(
                    ST_Distance(a.location, u.point)::numeric,
                    2
                ) AS distance_meters
            FROM merchants m
            CROSS JOIN user_point u
            JOIN LATERAL (
                SELECT
                    name,
                    location,
                    latitude,
                    longitude
                FROM merchant_agencies
                WHERE merchant_id = m.id
                AND search_text ILIKE CONCAT('%', :s, '%')
                ORDER BY location <-> u.point
                LIMIT :limit
            ) a ON TRUE
            ORDER BY distance_meters ASC
            """, nativeQuery = true)
    List<NearestAgencyProjection> searchNearestAgencies(
            @Param("s") String searchKeyword,
            @Param("userLat") double userLat,
            @Param("userLng") double userLng,
            @Param("limit") int limit);

    List<MerchantAgency> findByMerchantId(UUID merchantId);

  

}
