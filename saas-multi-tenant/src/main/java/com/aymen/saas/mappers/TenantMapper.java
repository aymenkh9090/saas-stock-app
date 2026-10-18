package com.aymen.saas.mappers;

import com.aymen.saas.entities.Tenant;
import com.aymen.saas.requests.RegisterTenantRequest;
import com.aymen.saas.responses.TenantResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TenantMapper {


    public Tenant toEntity(RegisterTenantRequest request) {

        return Tenant.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .email(request.getEmail())
                .adminFullName(request.getAdminFullName())
                .adminUsername(request.getAdminUsername())
                .adminEmail(request.getAdminEmail())
                .deleted(false)
                .build();
    }



    public TenantResponse toResponse(Tenant entity) {

        return TenantResponse.builder()
                .tenantId(entity.getId())
                .companyName(entity.getCompanyName())
                .companyCode(entity.getCompanyCode())
                .email(entity.getEmail())
                .adminFullName(entity.getAdminFullName())
                .adminUsername(entity.getAdminUsername())
                .adminEmail(entity.getAdminEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
