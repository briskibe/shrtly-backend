package com.poniansoft.shrtly.shortlink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    @Query("SELECT sl FROM ShortLink sl left join fetch sl.product p WHERE sl.shortCode = :shortCode AND sl.slug = :slug")
    ShortLink findByShortCodeAndSlug(@Param("shortCode") String shortCode, @Param("slug") String slug);

    @Query("SELECT sl FROM ShortLink sl WHERE sl.product.id = :productId")
    ShortLink findByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE ShortLink sl SET sl.slug = :slug WHERE sl.product.store.id = :storeId")
    void updateSlugInStore(@Param("slug") String shortLink, @Param("storeId") Long storeId);
}
