package ru.mediasoft.shop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mediasoft.shop.configuration.provider.CurrencyProvider;
import ru.mediasoft.shop.configuration.provider.CurrencySessionBean;

@Configuration
public class WebConfig {

    @Bean
    public CurrencyProvider currencyProvider(CurrencySessionBean currencySessionBean) {
        return new CurrencyProvider(currencySessionBean);
    }
}
