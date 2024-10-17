package ru.mediasoft.shop.service.criteriaDataType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.enumeration.OperationEnum;

public class CriteriaDataString extends CriteriaData<String> {
    public CriteriaDataString(String field, String value, OperationEnum operation) {
        super(field, value, operation);
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return switch (getOperation()) {
            case LIKE -> criteriaBuilder.like(
                    criteriaBuilder.toString(root.get(getField())),
                    "%" + getValue() + "%"
            );
            case EQUAL -> criteriaBuilder.equal(root.get(getField()), getValue());
            case GRATER_THAN_OR_EQ -> criteriaBuilder.greaterThanOrEqualTo(root.get(getField()), getValue());
            case LESS_THAN_OR_EQ -> criteriaBuilder.lessThanOrEqualTo(root.get(getField()), getValue());
        };
    }
}
