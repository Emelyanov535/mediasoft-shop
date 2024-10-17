package ru.mediasoft.shop.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public enum OperationEnum {
    EQUAL("=", "EQUAL", "eq"),
    GRATER_THAN_OR_EQ(">=", "GRATER_THAN_OR_EQ", "gte"),
    LESS_THAN_OR_EQ("<=", "LESS_THAN_OR_EQ", "lte"),
    LIKE("~", "LIKE", "contains");

    private final List<String> symbols;

    OperationEnum(String... symbols) {
        this.symbols = List.of(symbols);
    }

    @JsonCreator
    public static OperationEnum fromSymbol(String symbol) {
        for (OperationEnum operation : OperationEnum.values()) {
            if (operation.getSymbols().contains(symbol)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Unknown symbol: " + symbol);
    }
}
