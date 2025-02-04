package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.click.model.HistoricalClickDTO;
import com.poniansoft.shrtly.clickSummary.ClickSummary;
import com.poniansoft.shrtly.clickSummary.ClickSummaryRepository;
import com.poniansoft.shrtly.product.model.ProductDetailsDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ShortLinkRepository shortLinkRepository;
    private final ClickSummaryRepository clickSummaryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ShortLinkRepository shortLinkRepository, ClickSummaryRepository clickSummaryRepository) {
        this.productRepository = productRepository;
        this.shortLinkRepository = shortLinkRepository;
        this.clickSummaryRepository = clickSummaryRepository;
    }

    @Override
    @Transactional
    public List<Product> createProductsFromShopify(List<ShopifyProduct> shopifyProducts, Store store) {
        List<Product> products = shopifyProducts.stream().map(shopifyProduct -> {
            Product product = new Product();
            product.setStore(store);
            product.setProductId(shopifyProduct.getExternalId());
            product.setProductImageUrl(shopifyProduct.getImageUrl());
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

    @Override
    public ProductDetailsDTO getProductDetails(Long productId, Long storeId) {
        // Step 1: Fetch product details
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        if (product.getStore().getId() != storeId) {
            throw new IllegalArgumentException("Product does not belong to the store");
        }

        // Step 2: Fetch current short link details
        ShortLink currentShortLink = shortLinkRepository.findByProductId(productId);
        if (currentShortLink == null) {
            return null;
        }

        // Step 3: Fetch all historical click summaries
        List<ClickSummary> clickSummaries = clickSummaryRepository.findByProductId(productId);

        // Step 4: Aggregate clicks by slug and shortCode
        Map<String, HistoricalClickDTO> historicalClicks = new HashMap<>();
        int totalClicks = 0;

        for (ClickSummary summary : clickSummaries) {
            String key = summary.getSlug() + "-" + summary.getShortCode();  // Unique identifier for slug-shortCode combination

            // Aggregate clicks for each slug-shortCode pair
            historicalClicks.computeIfAbsent(key, k -> new HistoricalClickDTO(summary.getSlug(), summary.getShortCode(), 0));
            historicalClicks.get(key).addClicks(summary.getClickCount());

            totalClicks += summary.getClickCount();  // Total clicks across all slugs
        }

        // Step 5: Build the DTO with product details, current short link, and historical clicks
        return new ProductDetailsDTO(
                product.getId(),
                product.getProductName(),
                product.getProductUrl(),
                product.getProductImageUrl(),
                currentShortLink.getSlug(),
                currentShortLink.getShortCode(),
                currentShortLink.getSlug() + "/" + currentShortLink.getShortCode(),
                totalClicks,
                new ArrayList<>(historicalClicks.values())
        );
    }
}
