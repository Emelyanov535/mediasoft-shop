package ru.mediasoft.shop.service;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.controller.dto.ProductFilterDto;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.persistence.repository.ProductRepository;
import ru.mediasoft.shop.service.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    public List<ProductDto> simpleSearch(ProductFilterDto productFilterDto) {
        final PageRequest pageRequest = PageRequest.of(productFilterDto.getPage(), productFilterDto.getSize());

        final Specification<ProductEntity> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (productFilterDto.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + productFilterDto.getName().toLowerCase() + "%"));
            }
            if (productFilterDto.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), productFilterDto.getCategory()));
            }
            if (productFilterDto.getPrice() != null) {
                predicates.add(criteriaBuilder.equal(root.get("price"), productFilterDto.getPrice()));
            }
            if (productFilterDto.getAmount() != null && productFilterDto.getAmount() > 0) {
                predicates.add(criteriaBuilder.gt(root.get("amount"), productFilterDto.getAmount()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        final Page<ProductEntity> productEntities = productRepository.findAll(specification, pageRequest);

        return productEntities.stream()
                .map(productEntity -> conversionService.convert(productEntity, ProductDto.class))
                .toList();
    }

    public Page<ProductDto> hardSearch(Pageable pageable, List<CriteriaData<?>> criteriaDataList) {
        final Specification<ProductEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (CriteriaData<?> criteriaData : criteriaDataList) {
                predicates.add(criteriaData.buildPredicate(criteriaBuilder, root));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProductEntity> productEntities = productRepository.findAll(specification, pageable);

        return productEntities
                .map(productEntity -> conversionService.convert(productEntity, ProductDto.class));
    }
}
