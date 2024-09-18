package ru.mediasoft.shop.service.criteriaDataType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.enumeration.OperationEnum;
import ru.mediasoft.shop.enumeration.CategoryType;

public class CriteriaDataCategory extends CriteriaData<CategoryType> {
    public CriteriaDataCategory(@NotBlank(message = "Field cannot be blank") String field,
                                @NotNull(message = "Value cannot be null") CategoryType value,
                                @NotNull(message = "Operation cannot be null") OperationEnum operation) {
        super(field, value, operation);
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return switch (getOperation()) {
            case LIKE, EQUAL -> criteriaBuilder.equal(root.get(getField()), getValue());
            case GRATER_THAN_OR_EQ, LESS_THAN_OR_EQ -> throw new IllegalArgumentException("GRATER_THAN_OR_EQ operation is not supported for CategoryType");
        };
    }
}
