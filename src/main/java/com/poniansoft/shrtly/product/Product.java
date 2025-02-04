package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.shortlink.ShortLink;
import com.poniansoft.shrtly.store.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @jakarta.persistence.Index(name = "idx_products_store_id", columnList = "store_id")
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "PRODUCTS_SEQUENCE", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<ShortLink> shortLinks;

    @Column(name = "product_image_url", columnDefinition = "TEXT")
    private String productImageUrl;

    @Column(name = "product_id", nullable = false, length = 255)
    private Long productId; // Product ID from Shopify/WooCommerce

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_url", nullable = false, columnDefinition = "TEXT")
    private String productUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    void addShortLink(ShortLink shortLink) {
        shortLinks.add(shortLink);
        shortLink.setProduct(this);
    }

    void removeShortLink(ShortLink shortLink) {
        shortLinks.remove(shortLink);
        shortLink.setProduct(null);
    }

    void removeAllShortLinks() {
        shortLinks.forEach(shortLink -> shortLink.setProduct(null));
        shortLinks.clear();
    }
}
