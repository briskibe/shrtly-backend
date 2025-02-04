package com.poniansoft.shrtly.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductShortLink {
    private Long productId;
    private String productImageUrl;
    private String productName;
    private String originalUrl;
    private String slug;
    private String shortUrl;
    private int totalClicks;

    // Constructor matching the query exactly
    public ProductShortLink(Long productId, String productImageUrl, String productName, String originalUrl, String slug,
                            String shortUrl, Long totalClicks) {
        this.productId = productId;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.originalUrl = originalUrl;
        this.slug = slug;
        this.shortUrl = shortUrl;
        this.totalClicks = totalClicks != null ? totalClicks.intValue() : 0; // Avoid null issues
    }
}
