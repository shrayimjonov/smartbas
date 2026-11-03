package uz.davrbank.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public ApplicationRunner runLiquibaseAfterHibernate(
            DataSource dataSource) {
        return args -> {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
            liquibase.setDataSource(dataSource);
            liquibase.setShouldRun(true);
            liquibase.afterPropertiesSet(); // START liquibase
        };
    }

}
