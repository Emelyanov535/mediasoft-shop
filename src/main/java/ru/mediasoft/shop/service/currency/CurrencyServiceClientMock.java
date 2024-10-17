package ru.mediasoft.shop.service.currency;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(name = "rest.currency.mock", havingValue = "true")
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    @Override
    public ExchangeRateDto getExchangeRates() {
        ExchangeRateDto mockData = new ExchangeRateDto();
        mockData.setEUR(BigDecimal.valueOf(1.85));
        mockData.setUSD(BigDecimal.valueOf(1.30));
        mockData.setCNY(BigDecimal.valueOf(1.45));
        return mockData;
    }
}
