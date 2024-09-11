package ru.mediasoft.shop.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.scheduling")
@Getter
@Setter
public class SchedulerConfig {

    private String period;
    private Double priceIncreasePercentage;
    private boolean exclusiveLock;
}
