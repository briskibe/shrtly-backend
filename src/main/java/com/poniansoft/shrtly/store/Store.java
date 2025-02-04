package com.poniansoft.shrtly.store;

import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.user.User;
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
@Table(name = "stores", indexes = {
        @jakarta.persistence.Index(name = "idx_stores_user_id", columnList = "user_id")
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "store_id_generator")
    @SequenceGenerator(name = "store_id_generator", sequenceName = "STORE_SEQUENCE", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<Product> products;

    @Column(name = "platform", nullable = false, length = 50)
    private String platform;

    @Column(name = "store_name", nullable = false, length = 255)
    private String storeName;

    @Column(name = "store_url", nullable = false, length = 255)
    private String storeUrl;

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    void addProduct(Product product) {
        products.add(product);
        product.setStore(this);
    }

    void removeProduct(Product product) {
        products.remove(product);
        product.setStore(null);
    }

    void removeAllProducts() {
        products.forEach(product -> product.setStore(null));
        products.clear();
    }
}
