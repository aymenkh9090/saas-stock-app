package com.aymen.saas.services.impl;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.entities.Tenant;
import com.aymen.saas.entities.TenantStatus;
import com.aymen.saas.entities.User;
import com.aymen.saas.entities.UserRole;
import com.aymen.saas.exceptions.DuplicateRessourceException;
import com.aymen.saas.exceptions.InvalidRequestException;
import com.aymen.saas.mappers.TenantMapper;
import com.aymen.saas.repositories.TenantRepository;
import com.aymen.saas.repositories.UserRepository;
import com.aymen.saas.requests.RegisterTenantRequest;
import com.aymen.saas.responses.TenantResponse;
import com.aymen.saas.services.ProvisioningService;
import com.aymen.saas.services.TenantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {


    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProvisioningService provisioningService;

    @Override
    public void registerTenant(RegisterTenantRequest request) {
        // check if tenant exists by company code
        if(this.tenantRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new DuplicateRessourceException("Company Code already exists");
        }
        // check if tenant exists by mail
        if(this.tenantRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateRessourceException("Email already exists");
        }
        final Tenant tenant = this.tenantMapper.toEntity(request);
        tenant.setAdminPassword(passwordEncoder.encode(tenant.getAdminPassword()));
        tenant.setStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);

    }

    @Override
    public void approveTenant(String tenantId) {
        // check if the tenant exists
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(()->new EntityNotFoundException("Tenant not found"));
        tenant.setStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);

        try {
            // provision the schema for the tenant
            this.provisioningService.provisionTenant(tenant);
            // create the admin user for the tenant
            createAdminUser(tenant);

        }catch (final Exception ex){
            rollBackTenantStatus(tenant);
        }

    }




    @Override
    public void activateTenant(String tenantId) {
        final Tenant tenant =
                this.tenantRepository.findById(tenantId)
                        .orElseThrow(()->new EntityNotFoundException("Tenant not found"));
        if(tenant.getStatus() != TenantStatus.PENDING) {
            throw new InvalidRequestException("Tenant is not pending status");
        }
        tenant.setStatus(TenantStatus.ACTIVE);
        this.tenantRepository.save(tenant);
    }

    @Override
    public void deactivateTenant(String tenantId) {
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(()->new EntityNotFoundException("Tenant not found"));
        if(tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not active status");
        }
        tenant.setStatus(TenantStatus.INACTIVE);
        this.tenantRepository.save(tenant);
    }

    @Override
    public void suspendTenant(String tenantId) {
        final Tenant tenant = this.tenantRepository.findById(tenantId)
                .orElseThrow(()->new EntityNotFoundException("Tenant not found"));
        if(tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new InvalidRequestException("Tenant is not active status");
        }
        tenant.setStatus(TenantStatus.SUSPENDED);
        this.tenantRepository.save(tenant);

    }

    @Override
    public PageResponse<TenantResponse> findAll(int page, int size) {
        final PageRequest pageRequest =  PageRequest.of(page, size);
        final Page<Tenant> tenants = this.tenantRepository.findAll(pageRequest);
        final Page<TenantResponse> tenantResponses = tenants.map(this.tenantMapper::toResponse);
        return PageResponse.of(tenantResponses);
    }


    private void createAdminUser(Tenant tenant) {

        // check if the admin user exist
        if(this.userRepository.existsByUsername(tenant.getAdminUsername())) {
            throw new DuplicateRessourceException("Username already exists");
        }

        final User adminUser = User.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getAdminEmail())
                .firstName(extractFirstName(tenant.getAdminUsername()))
                .lastName(extractLastName(tenant.getAdminUsername()))
                .password(tenant.getAdminPassword())
                .role(UserRole.ROLE_PLATFORM_ADMIN)
                .tenant(tenant)
                .deleted(false)
                .build();

        this.userRepository.save(adminUser);
        log.info("Admin user created");
    }

    private String extractLastName(final String adminFullName) {
        return adminFullName.split("").length > 1 ? adminFullName.split(" ")[1] : adminFullName;
    }

    private String extractFirstName(final String adminFullName) {
        return adminFullName.split(" ")[0];
    }
    private void rollBackTenantStatus(Tenant tenant) {
        tenant.setStatus(TenantStatus.PENDING);
        this.tenantRepository.save(tenant);
    }


}
