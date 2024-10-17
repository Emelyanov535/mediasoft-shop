package ru.mediasoft.shop.configuration.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class CustomerIdRequestBean {
    private Long customerId;
}
