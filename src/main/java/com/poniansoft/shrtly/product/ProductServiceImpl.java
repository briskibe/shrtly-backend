package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.store.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public List<Product> createProductsFromShopify(List<ShopifyProduct> shopifyProducts, Store store) {
        List<Product> products = shopifyProducts.stream().map(shopifyProduct -> {
            Product product = new Product();
            product.setStore(store);
            product.setProductId(shopifyProduct.getExternalId());
            product.setProductName(shopifyProduct.getTitle());
            product.setProductUrl(shopifyProduct.getUrl());
            return product;
        }).collect(Collectors.toList());

        return productRepository.saveAll(products);
    }
}
