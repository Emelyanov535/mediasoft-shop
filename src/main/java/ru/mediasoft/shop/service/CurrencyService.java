package ru.mediasoft.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.mediasoft.shop.configuration.properties.CurrencyConfig;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Service
public class CurrencyService {

    private final CurrencyConfig currencyConfig;

    private final WebClient webClient;

    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    public CurrencyService(CurrencyConfig currencyConfig, WebClient.Builder webClientBuilder, ObjectMapper objectMapper, CacheManager cacheManager) {
        this.currencyConfig = currencyConfig;
        this.cacheManager = cacheManager;
        this.webClient = webClientBuilder
                .baseUrl(currencyConfig.getHost())
                .build();
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "currency")
    public ExchangeRateDto getCurrentCurrency() {
        System.out.println("мы получает курс");
        return webClient.get()
                .uri(currencyConfig.getMethods().getGetExchangeRates())
                .retrieve()
                .bodyToMono(ExchangeRateDto.class)
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500))
                        .filter(throwable -> throwable instanceof WebClientResponseException.BadRequest)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new RuntimeException("API call failed after retries", retrySignal.failure())
                        )
                )
                .onErrorResume(throwable -> {
                    try {
                        return Mono.just(getFallbackExchangeRate());
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Failed to load fallback exchange rates", e));
                    }
                }).block();
    }

    public BigDecimal convertPrice(BigDecimal price, String currency, ExchangeRateDto exchangeRateDto) {
        BigDecimal conversionRate = switch (currency) {
            case "EUR" -> exchangeRateDto.getEUR();
            case "USD" -> exchangeRateDto.getUSD();
            case "CNY" -> exchangeRateDto.getCNY();
            case "RUB" -> BigDecimal.valueOf(1);
            default -> throw new IllegalArgumentException("Unsupported currency: " + currency);
        };

        return price.divide(conversionRate, 2, RoundingMode.DOWN);
    }

    private ExchangeRateDto getFallbackExchangeRate() throws IOException {
        ClassPathResource resource = new ClassPathResource("exchange-rate.json");
        return objectMapper.readValue(resource.getInputStream(), ExchangeRateDto.class);
    }
}
