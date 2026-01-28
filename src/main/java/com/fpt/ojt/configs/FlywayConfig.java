package com.fpt.ojt.configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String locations;

    @Value("${spring.flyway.baseline-on-migrate:true}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.schemas:public}")
    private String[] schemas;

    @Value("${spring.flyway.default-schema:public}")
    private String defaultSchema;

    @Value("${spring.flyway.clean-disabled:true}")
    private boolean cleanDisabled;

    @Value("${spring.flyway.validate-on-migrate:false}")
    private boolean validateOnMigrate;

    // Make this Bean have initMethod = "migrate" to avoid Hibernate run and validate before Flyway run
    @Bean(initMethod = "migrate")
    @DependsOn("dataSource")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .schemas(schemas)
                .defaultSchema(defaultSchema)
                .cleanDisabled(cleanDisabled)
                .validateOnMigrate(validateOnMigrate)
                .load();
    }
}
