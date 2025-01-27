package com.poniansoft.shrtly.user;

import com.poniansoft.shrtly.shortlink.ShortLink;
import com.poniansoft.shrtly.store.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(name = "externalId", nullable = false, length = 255)
    private String externalId; // Firebase UID

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Store> stores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<ShortLink> shortLinks;

    void addStore(Store store) {
        stores.add(store);
        store.setUser(this);
    }

    void removeStore(Store store) {
        stores.remove(store);
        store.setUser(null);
    }

    void removeAllStores() {
        stores.forEach(store -> store.setUser(null));
        stores.clear();
    }
}
