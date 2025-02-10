package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.user.User;
import com.poniansoft.shrtly.util.Base62Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShortLinkServiceImpl implements ShortLinkService {
    private final ShortLinkRepository shortLinkRepository;

    public ShortLinkServiceImpl(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    @Override
    @Transactional
    public void createShortLinksFromProducts(List<Product> productList, User user) {
        List<ShortLink> shortLinks = productList.stream().map(product -> {
            ShortLink shortLink = new ShortLink();
            shortLink.setProduct(product);
            shortLink.setUser(user);
            shortLink.setSlug(product.getStore().getStoreName());
            String shortCode = generateShortCode(product.getProductId());
            shortLink.setShortCode(shortCode);
            shortLink.setLongUrl(product.getProductUrl());
            return shortLink;
        }).collect(Collectors.toList());

        shortLinkRepository.saveAll(shortLinks);
    }

    // Short Code Generator
    private String generateShortCode(Long productId) {
        String uniqueInput = productId + "-" + System.nanoTime();
        return Base62Util.encode(uniqueInput.hashCode());
    }

    public ShortLink findByShortCode(String shortCode, String slug) {
        return shortLinkRepository.findByShortCodeAndSlug(shortCode, slug);
    }
}
