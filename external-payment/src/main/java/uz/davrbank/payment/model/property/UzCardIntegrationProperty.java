package uz.davrbank.payment.model.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.uzcard")
@EqualsAndHashCode(callSuper = true)
public class UzCardIntegrationProperty extends BaseProperty {
}
