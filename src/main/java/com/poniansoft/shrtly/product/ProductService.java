package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.product.model.ProductShortLink;
import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.store.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> createProductsFromShopify(List<ShopifyProduct> shopifyProducts, Store store);
    Page<ProductShortLink> getProductsWithShortLinks(Long storeId, int page, int size);
    ProductShortLink getProductWithShortLinksAndClicksByProductId(Long productId);
    void updateStoreSlug(Long storeId, String slug);
    void updateProductSlug(Long product, String slug, String shortLink);
}
