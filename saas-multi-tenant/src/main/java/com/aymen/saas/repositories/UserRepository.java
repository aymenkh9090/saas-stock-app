package com.aymen.saas.repositories;

import com.aymen.saas.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findByIdAndNotDeleted(String id);


    @Query("SELECT u FROM User u WHERE u.tenant.id = :tenantId AND u.deleted = false")
    Page<User> findAllByTenantId(String tenantId, Pageable pageable);


}
