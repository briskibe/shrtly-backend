package com.poniansoft.shrtly.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u left join fetch u.stores st WHERE u.externalId = :externalId")
    User findByExternalId(String externalId);
}
