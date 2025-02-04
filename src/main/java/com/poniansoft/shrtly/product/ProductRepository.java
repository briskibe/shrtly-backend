package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.product.model.ProductShortLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
        SELECT new com.poniansoft.shrtly.product.model.ProductShortLink(
            p.id, p.productImageUrl, p.productName, p.productUrl, sl.slug, sl.shortCode,
            COALESCE(SUM(c.clickCount), 0) as totalClicks)
        FROM Product p
        LEFT JOIN ShortLink sl ON p.id = sl.product.id
        LEFT JOIN ClickSummary c ON sl.id = c.shortLink.id
        WHERE p.store.id = :storeId
        GROUP BY p.id, p.productName, p.productUrl, sl.slug, sl.shortCode
        order by totalClicks desc
    """)
    Page<ProductShortLink> findProductsWithShortLinksAndClicks(@Param("storeId") Long storeId, Pageable pageable);

    @Query("""
        SELECT new com.poniansoft.shrtly.product.model.ProductShortLink(
            p.id, p.productImageUrl, p.productName, p.productUrl, sl.slug, sl.shortCode,
            COALESCE(SUM(c.clickCount), 0) as totalClicks)
        FROM Product p
        LEFT JOIN ShortLink sl ON p.id = sl.product.id
        LEFT JOIN ClickSummary c ON sl.id = c.shortLink.id
        WHERE p.id = :productId
        GROUP BY p.id, p.productName, p.productUrl, sl.slug, sl.shortCode
        order by totalClicks desc
    """)
    ProductShortLink findProductWithShortLinksAndClicksByProductId(@Param("productId") Long productId);

    @Query("SELECT p.productId FROM Product p where p.store.id = :storeId")
    List<Long> findAllProductIds(@Param("storeId") Long storeId);
}
