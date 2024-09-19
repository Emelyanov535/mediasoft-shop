package ru.mediasoft.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.mediasoft.shop.configuration.properties.CurrencyConfig;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Service
public class CurrencyService {

    private final CurrencyConfig currencyConfig;

    private final WebClient webClient;

    public CurrencyService(CurrencyConfig currencyConfig, WebClient.Builder webClientBuilder) {
        this.currencyConfig = currencyConfig;
        this.webClient = webClientBuilder
                .baseUrl(currencyConfig.getHost())
                .build();
    }

    public Mono<ExchangeRateDto> getCurrentCurrency() {
        return webClient.get()
                .uri(currencyConfig.getMethods().getGetExchangeRates())
                .retrieve()
                .bodyToMono(ExchangeRateDto.class)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.BadRequest)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new RuntimeException("API call failed after retries", retrySignal.failure())
                        )
                );
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
}
