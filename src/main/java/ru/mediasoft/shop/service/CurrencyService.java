package ru.mediasoft.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.service.currency.CurrencyServiceClient;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final CurrencyServiceClient currencyServiceClient;

    @Cacheable(value = "currency")
    public ExchangeRateDto getCurrentCurrency() {
        System.out.println("мы получаем курс");
        return currencyServiceClient.getExchangeRates();
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
