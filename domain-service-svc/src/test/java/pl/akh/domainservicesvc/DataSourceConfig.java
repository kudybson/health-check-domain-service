package pl.akh.domainservicesvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Configuration
@EnableJpaRepositories(basePackages = "pl.akh.domainservicesvc.repository")
@TestPropertySource(locations = "application-test.yml")
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    String driver;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Profile("testcontainers")
    public DataSource dataSourceContainer() {
        OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
                .withDatabaseName("healthcheck")
                .withUsername("testUser")
                .withPassword("testPassword");
        oracle.start();
        oracle.waitingFor(
                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(180L)));
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(oracle.getDriverClassName());
        dataSource.setUrl(oracle.getJdbcUrl());
        dataSource.setUsername(oracle.getUsername());
        dataSource.setPassword(oracle.getPassword());
        return dataSource;
    }
}
