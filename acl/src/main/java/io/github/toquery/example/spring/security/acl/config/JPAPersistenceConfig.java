package io.github.toquery.example.spring.security.acl.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
// @PropertySource("classpath:io.github.toquery.example.spring.security.acl.datasource.properties")
@EnableJpaRepositories(basePackages = "io.github.toquery.example.spring.security.acl.persistence.dao")
@EntityScan(basePackages = "io.github.toquery.example.spring.security.acl.persistence.entity")
public class JPAPersistenceConfig {
}
