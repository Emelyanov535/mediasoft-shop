package ru.mediasoft.shop.service.currency;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import ru.mediasoft.shop.configuration.properties.RestConfig;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.time.Duration;

@Service
@AllArgsConstructor
@ConditionalOnProperty(name = "rest.currency.mock", havingValue = "false")
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final RestConfig restConfig;
    private final WebClient webClient;

    @Override
    @Cacheable(value = "currency")
    public ExchangeRateDto getExchangeRates() {
        System.out.println("мы получаем курс c другого сервиса");
        return webClient.get()
                .uri(restConfig.getCurrency().getMethods().getGetExchangeRates())
                .retrieve()
                .bodyToMono(ExchangeRateDto.class)
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500))
                        .filter(throwable -> throwable instanceof WebClientResponseException.BadRequest)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new RuntimeException("API call failed after retries", retrySignal.failure())
                        )
                ).block();
    }
}
