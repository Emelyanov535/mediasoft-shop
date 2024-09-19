package ru.mediasoft.shop.configuration;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mediasoft.shop.configuration.provider.CurrencyProvider;
import ru.mediasoft.shop.configuration.provider.CurrencySessionBean;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig {

    @Bean
    public CurrencyProvider currencyProvider(CurrencySessionBean currencySessionBean) {
        return new CurrencyProvider(currencySessionBean);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(1, TimeUnit.MINUTES)
                                .build().asMap(),
                        false
                );
            }
        };
    }
}
