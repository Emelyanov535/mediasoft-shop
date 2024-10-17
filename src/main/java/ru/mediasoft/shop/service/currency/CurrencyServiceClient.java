package ru.mediasoft.shop.service.currency;

import ru.mediasoft.shop.service.dto.ExchangeRateDto;

public interface CurrencyServiceClient {
    ExchangeRateDto getExchangeRates();
}
