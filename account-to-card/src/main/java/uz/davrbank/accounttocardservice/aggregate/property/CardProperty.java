package uz.davrbank.accounttocardservice.aggregate.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "internal.card")
public class CardProperty {

    private CardRegex regex;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardRegex {
        private String token;
        private String pan;
        private String expiry;
    }
}
