package uz.davrbank.absresourceaccesshubservice.aggregate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.davrbank.absresourceaccesshubservice.service.CoreFundamentalUniversal;

@Configuration
public class CoreFundamentalUniversalConfig {

    @Bean
    public CoreFundamentalUniversal coreFundamentalUniversal(
            ObjectMapper objectMapper,
            OracleDatabaseConfig oracleDatabaseConfig
    ) {
        return new CoreFundamentalUniversal(objectMapper, oracleDatabaseConfig);
    }
}
