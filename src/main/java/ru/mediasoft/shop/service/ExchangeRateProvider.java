package ru.mediasoft.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.service.currency.CurrencyServiceClient;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class ExchangeRateProvider {

    private final CurrencyServiceClient currencyServiceClient;
    private final ObjectMapper objectMapper;

    private ExchangeRateDto getExchangeRate() {
        try {
            return currencyServiceClient.getExchangeRates();
        } catch (Exception e) {
            try {
                return getExchangeRateFromFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public BigDecimal convertPrice(BigDecimal price, String currency) {
        ExchangeRateDto exchangeRate = getExchangeRate();
        BigDecimal conversionRate = switch (currency) {
            case "EUR" -> exchangeRate.getEUR();
            case "USD" -> exchangeRate.getUSD();
            case "CNY" -> exchangeRate.getCNY();
            case "RUB" -> BigDecimal.valueOf(1);
            default -> throw new IllegalArgumentException("Unsupported currency: " + currency);
        };

        return price.divide(conversionRate, 2, RoundingMode.DOWN);
    }


    private ExchangeRateDto getExchangeRateFromFile() throws IOException {
        System.out.println("мы получаем курс c файла");
        ClassPathResource resource = new ClassPathResource("exchange-rate.json");
        return objectMapper.readValue(resource.getInputStream(), ExchangeRateDto.class);
    }
}
