package ru.mediasoft.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExchangeRateDto {
    private BigDecimal EUR;
    private BigDecimal USD;
    private BigDecimal CNY;
}
