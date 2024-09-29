package ru.mediasoft.shop.configuration;

import com.google.common.cache.CacheBuilder;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mediasoft.shop.configuration.filter.HeaderValidationFilter;
import ru.mediasoft.shop.configuration.properties.RestConfig;

import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class WebConfig {

    private final RestConfig restConfig;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected @NonNull Cache createConcurrentMapCache(@NonNull String name) {
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

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(restConfig.getCurrency().getHost())
                .build();
    }

    @Bean
    public FilterRegistrationBean<HeaderValidationFilter> loggingFilter(HeaderValidationFilter headerValidationFilter){
        FilterRegistrationBean<HeaderValidationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(headerValidationFilter);
        registrationBean.addUrlPatterns(
                "/api/v1/order/*",
                "/api/v1/order"
        );

        return registrationBean;
    }
}
