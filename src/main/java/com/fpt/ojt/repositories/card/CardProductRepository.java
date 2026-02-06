package com.fpt.ojt.repositories.card;

import com.fpt.ojt.models.postgres.card.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, UUID>, JpaSpecificationExecutor<CardProduct> {
    @Query(value = """
                SELECT *
                FROM card_products
                WHERE deleted_at IS NULL
                  AND search_text % lower(:q)
                ORDER BY search_text <-> lower(:q) ASC
                LIMIT :limit
            """, nativeQuery = true)
    List<CardProduct> search(@Param("q") String keyword,
            @Param("limit") int limit);
}
