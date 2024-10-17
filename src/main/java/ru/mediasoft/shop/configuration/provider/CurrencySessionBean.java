package ru.mediasoft.shop.configuration.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
public class CurrencySessionBean {
    private static final String DEFAULT_CURRENCY = "RUB";
    private String currency = DEFAULT_CURRENCY;
}
