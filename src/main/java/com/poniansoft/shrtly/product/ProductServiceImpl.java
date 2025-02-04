package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.product.model.ProductShortLink;
import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.shortlink.ShortLink;
import com.poniansoft.shrtly.shortlink.ShortLinkRepository;
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
    private final ShortLinkRepository shortLinkRepository;

    public ProductServiceImpl(ProductRepository productRepository, ShortLinkRepository shortLinkRepository) {
        this.productRepository = productRepository;
        this.shortLinkRepository = shortLinkRepository;
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

    @Override
    public ProductShortLink getProductWithShortLinksAndClicksByProductId(Long productId) {
        return productRepository.findProductWithShortLinksAndClicksByProductId(productId);
    }


    @Override
    @Transactional
    public void updateStoreSlug(Long storeId, String slug) {
        shortLinkRepository.updateSlugInStore(slug, storeId);
    }

    @Override
    @Transactional
    public void updateProductSlug(Long product, String slug, String shortLink) {
        ShortLink sl = shortLinkRepository.findByProductId(product);
        if (sl == null)
            return;

        if (slug != null) {
            sl.setSlug(slug);
        }

        if (shortLink != null) {
            sl.setShortCode(shortLink);
        }
    }
}
