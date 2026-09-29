package com.aymen.saas.repositories;

import com.aymen.saas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
