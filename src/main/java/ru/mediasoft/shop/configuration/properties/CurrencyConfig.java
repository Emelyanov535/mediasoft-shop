package ru.mediasoft.shop.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "currency-service")
@Getter
@Setter
public class CurrencyConfig {

    private String host;
    private Methods methods;

    @Getter
    @Setter
    public static class Methods {
        private String getExchangeRates;
    }
}
