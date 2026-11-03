package uz.davrbank.zalogautohubservice.aggregate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Data
@Configuration
public class OracleDatabaseConfig {

    @Value("${spring.datasource.url}")
    private String serverUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(serverUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("oracle.jdbc.OracleDriver");

        return ds;
    }
}
