package com.aymen.saas.services.impl;

import com.aymen.saas.entities.Tenant;
import com.aymen.saas.exceptions.TenantProvisioningException;
import com.aymen.saas.services.ProvisioningService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void provisionTenant(Tenant tenant) {

        final String schemaName = "tenant_" + tenant.getCompanyCode().toLowerCase();
        try {

            log.info("Provisioning tenant {} ( schemaName ; {} ) " , tenant.getCompanyName(),schemaName);
            // 1- Create the postgresql schema
            createSchema(schemaName);
            log.info("Schema created Successfully {} " , schemaName);
            // 2- Run the flyway migration for this schema
            runTenantMigration(schemaName);
            log.info("Schema migrated successfully {} " , schemaName);
            // 3- Initialise the default data (optional)
            initializeDefaultData(schemaName,tenant);


        } catch (Exception e) {
            log.error("Failed to provision tenant {} " , tenant.getCompanyName() , e);

            // rollback ==> drop schema creation
            try{
                dropSchema(schemaName);
                log.info("Schema dropped Successfully {} " , schemaName);
            } catch(final Exception e1) {
                log.error("Failed to rollback schema creation for tenant: {}", tenant.getCompanyName(), e);
            }
            throw new TenantProvisioningException("Failed to provision tenant");
        }


    }

    private void dropSchema(String schemaName) {
        final String sql = String.format("DROP SCHEMA IF EXISTS %s", schemaName);
        jdbcTemplate.execute(sql);
    }

    private void initializeDefaultData( String schemaName,Tenant tenant) {
        log.info("Initializing default data for tenant: {}", tenant.getCompanyName());
        // here you can add default data initialization code
    }

    private void runTenantMigration(String schemaName) {
        log.info("Running tenant migration for tenant {} ", schemaName);
        final Flyway tenantFlyway = Flyway.configure()
                .dataSource(this.dataSource)
                .schemas(schemaName)
                .locations("classpath:db/migration/tenant")
                .baselineOnMigrate(true)
                .table("flyway_schema_history")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .load();
        log.info("Tenant migration for {} started  " , schemaName);
        tenantFlyway.migrate();
        log.info("Tenant migration for {} finished  " , schemaName);
    }

    private void createSchema(String schemaName) {
        final String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s;", schemaName);
        this.jdbcTemplate.execute(sql);
    }
}
