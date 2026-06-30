package com.aymen.saas.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;



// this classe that enable automatically our filter declared
@Aspect
@Component
public class TenantHibernateFilter {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.aymen.saas.services.impl.*.*(..))")
    public void activateFilter(){
        final String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            final Session session = entityManager.unwrap(Session.class);

            // activate the filter to inject tenantId
            session.enableFilter("TenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }




}
