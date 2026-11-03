package uz.davrbank.remittanceoutside.aggregate.configuration.clickhouse;

import com.clickhouse.jdbc.ClickHouseDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 14.11.2025
 * Time: 10:52
 * Project: remittance-outside
 */

@Configuration
public class ClickhouseConfiguration {

    @Value("${spring.clickhouse.url}")
    private String clickhouseUrl;

    @Value("${spring.clickhouse.username:}")
    private String clickhouseUsername;

    @Value("${spring.clickhouse.password:}")
    private String clickHousePassword;

    @Bean("clickhouseDataSource")
    public HikariDataSource clickhouseDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(clickhouseUrl);
        hikariConfig.setUsername(clickhouseUsername);
        hikariConfig.setPassword(clickHousePassword);
        hikariConfig.setDriverClassName(ClickHouseDriver.class.getName());
        hikariConfig.setMinimumIdle(20);
        hikariConfig.setMaximumPoolSize(200);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "clickHouseJdbcTemplate")
    public JdbcTemplate clickHouseJdbcTemplate(@Qualifier("clickhouseDataSource") HikariDataSource clickHouseDataSource) {
        return new JdbcTemplate(clickHouseDataSource);
    }
}
