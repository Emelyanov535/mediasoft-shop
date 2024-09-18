package ru.mediasoft.shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.mediasoft.shop.persistence.entity.ProductEntity;

import java.util.UUID;

public interface ProductRepository extends JpaRepositoryImplementation<ProductEntity, UUID> {
    Page<ProductEntity> findAll(Pageable pageable);
    boolean existsByArticle(Long article);
}