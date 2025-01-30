package com.poniansoft.shrtly.shopify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopifyProduct {
    private Long externalId;
    private String title;
    private String url;
}
