package com.fpt.ojt.repositories.deal;

import com.fpt.ojt.models.postgres.deal.MerchantDeal;
import com.fpt.ojt.models.postgres.merchant.MerchantDealFlatProjection;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantDealRepository
    extends JpaRepository<MerchantDeal, UUID>, JpaSpecificationExecutor<MerchantDeal> {

  @EntityGraph(attributePaths = { "merchantAgency", "merchantAgency.merchant" })
  @Query("""
      SELECT md FROM MerchantDeal md
      WHERE md.deletedAt IS NULL
        AND md.validFrom <= CURRENT_DATE
        AND md.validTo >= CURRENT_DATE
      """)
  List<MerchantDeal> findAllAvailableMerchantDeals();

  @Query(value = """
      SELECT
          ma.id AS agencyId,
          ma.name AS agencyName,
          ma.latitude AS latitude,
          ma.longitude AS longitude,

          m.mcc AS merchantMcc,
          m.image_url AS merchantLogoUrl,

          md.id AS dealId,
          md.deal_name AS dealName,
          md.discount_rate AS discountRate,
          md.cashback_rate AS cashbackRate,
          md.points_multiplier AS pointsMultiplier,
          md.description AS description,
          md.valid_from AS validFrom,
          md.valid_to AS validTo,

          ST_Distance(
              ma.location,
              ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
          ) AS distanceMeters

      FROM merchant_deals md
      JOIN merchant_agencies ma ON md.merchant_agency_id = ma.id
      JOIN merchants m ON ma.merchant_id = m.id
      WHERE md.deleted_at IS NULL
        AND ma.deleted_at IS NULL
        AND md.valid_from <= CURRENT_DATE
        AND md.valid_to >= CURRENT_DATE
        AND ST_DWithin(
            ma.location,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
            :radiusMeters
        )
      ORDER BY distanceMeters ASC
      """, nativeQuery = true)
  List<MerchantDealFlatProjection> findAvailableDealsInRadius(
      @Param("latitude") double latitude,
      @Param("longitude") double longitude,
      @Param("radiusMeters") int radiusMeters);
}
