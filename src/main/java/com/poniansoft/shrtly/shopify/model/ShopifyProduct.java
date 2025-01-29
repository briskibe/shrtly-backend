package com.poniansoft.shrtly.shopify.model;

import lombok.Data;

@Data
public class ShopifyProduct {
    private Long externalId;
    private String title;
    private String url;
}
