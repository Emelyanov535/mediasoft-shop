package ru.mediasoft.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mediasoft.shop.enumeration.OperationEnum;
import ru.mediasoft.shop.service.criteriaDataType.*;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        property = "field")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriteriaDataBigDecimal.class, name = "price"),
        @JsonSubTypes.Type(value = CriteriaDataInteger.class, name = "amount"),
        @JsonSubTypes.Type(value = CriteriaDataString.class, name = "name"),
        @JsonSubTypes.Type(value = CriteriaDataLocalDate.class, name = "createdAt"),
        @JsonSubTypes.Type(value = CriteriaDataCategory.class, name = "category")
})
@AllArgsConstructor
public abstract class CriteriaData<T> {
    @NotBlank(message = "Field cannot be blank")
    private String field;
    @NotNull(message = "Value cannot be null")
    private T value;
    @NotNull(message = "Operation cannot be null")
    private OperationEnum operation;

    public abstract Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root);
}
