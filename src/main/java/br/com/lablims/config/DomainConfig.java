package br.com.lablims.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;

@Configuration
@EntityScan("br.com.lablims.domain")
@EnableJpaRepositories("br.com.lablims.repos")
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableWebSecurity
@EnableEnversRepositories
public class DomainConfig {
}
