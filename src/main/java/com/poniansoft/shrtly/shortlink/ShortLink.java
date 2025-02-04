package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "short_links", indexes = {
        @jakarta.persistence.Index(name = "idx_short_links_user_id", columnList = "user_id"),
        @jakarta.persistence.Index(name = "idx_short_links_product_id", columnList = "product_id")
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class ShortLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "shortlink_id_generator")
    @SequenceGenerator(name = "shortlink_id_generator", sequenceName = "SHORTLINK_SEQUENCE", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "short_code", nullable = false, length = 50)
    private String shortCode;

    @Column(name = "slug", nullable = false, length = 50)
    private String slug;

    @Column(name = "long_url", nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
