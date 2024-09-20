package ru.mediasoft.shop.service.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.mediasoft.shop.configuration.properties.CurrencyConfig;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.io.IOException;
import java.time.Duration;

@Service
@ConditionalOnProperty(name = "currency-service.mock", havingValue = "false")
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final CurrencyConfig currencyConfig;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public CurrencyServiceClientImpl(CurrencyConfig currencyConfig, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.currencyConfig = currencyConfig;
        this.webClient = webClientBuilder
                .baseUrl(currencyConfig.getHost())
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public ExchangeRateDto getExchangeRates() {
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


    private ExchangeRateDto getFallbackExchangeRate() throws IOException {
        ClassPathResource resource = new ClassPathResource("exchange-rate.json");
        return objectMapper.readValue(resource.getInputStream(), ExchangeRateDto.class);
    }
}
