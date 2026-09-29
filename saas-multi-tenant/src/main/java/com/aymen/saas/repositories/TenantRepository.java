package com.aymen.saas.repositories;

import com.aymen.saas.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {

    boolean existsByEmail(String email);
    boolean existsByCompanyCode(String companyCode);



}
