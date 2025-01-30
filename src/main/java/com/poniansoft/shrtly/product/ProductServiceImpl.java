package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.product.model.ProductShortLink;
import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<ProductShortLink> getProductsWithShortLinks(Long storeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "totalClicks"));
        return productRepository.findProductsWithShortLinksAndClicks(storeId, pageable);
    }
}
