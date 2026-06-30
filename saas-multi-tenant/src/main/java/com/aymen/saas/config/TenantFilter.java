package com.aymen.saas.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter  implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        // caster servletRequest and servletREsponse to an HTPP request and response *
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String tenantId = resolverHeader(request);
        if(tenantId == null || tenantId.isBlank()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\" error\" : \" Tenant Id is missing in the request geader ,Pleade add yhe header X-TENANT-ID.\" }");
            return;
        }
        try{
            TenantContext.setCurrentTenant(tenantId);
            chain.doFilter(servletRequest,servletResponse);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolverHeader(HttpServletRequest request) {
        final String tenantId = request.getHeader(TENANT_HEADER);
        if(tenantId!=null && !tenantId.isBlank() ){
            return tenantId;
        }
        return null ;
    }
}
