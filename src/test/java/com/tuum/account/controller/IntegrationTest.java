package com.tuum.account.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = Initializer.class)
@Sql(scripts = "/clear_database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public @interface IntegrationTest {
}

class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> POSTGRES_DB_CONTAINER = new PostgreSQLContainer<>("postgres:16");

    static {
        POSTGRES_DB_CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRES_DB_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRES_DB_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_DB_CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
