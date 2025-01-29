package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.store.Store;

import java.util.List;

public interface ProductService {
    List<Product> createProductsFromShopify(List<ShopifyProduct> shopifyProducts, Store store);
}
