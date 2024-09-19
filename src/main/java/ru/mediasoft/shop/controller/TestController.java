package ru.mediasoft.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.mediasoft.shop.service.CurrencyService;
import ru.mediasoft.shop.service.dto.ExchangeRateDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final CurrencyService currencyService;

    @GetMapping("/exchange-rates")
    public Mono<ExchangeRateDto> getExchangeRates() {
        return currencyService.getCurrentCurrency();
    }
}
