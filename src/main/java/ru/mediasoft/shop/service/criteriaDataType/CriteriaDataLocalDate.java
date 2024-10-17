package ru.mediasoft.shop.service.criteriaDataType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.enumeration.OperationEnum;

import java.time.LocalDate;

public class CriteriaDataLocalDate extends CriteriaData<LocalDate> {
    public CriteriaDataLocalDate(String field, LocalDate value, OperationEnum operation) {
        super(field, value, operation);
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return switch (getOperation()) {
            case LIKE -> criteriaBuilder.between(root.get(getField()), getValue().minusDays(5), getValue().plusDays(5));
            case EQUAL -> criteriaBuilder.equal(root.get(getField()), getValue());
            case GRATER_THAN_OR_EQ -> criteriaBuilder.greaterThanOrEqualTo(root.get(getField()), getValue());
            case LESS_THAN_OR_EQ -> criteriaBuilder.lessThanOrEqualTo(root.get(getField()), getValue());
        };
    }
}
