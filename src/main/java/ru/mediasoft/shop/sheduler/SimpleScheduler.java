package ru.mediasoft.shop.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ru.mediasoft.shop.annotation.TimeMeter;
import ru.mediasoft.shop.persistence.entity.ProductEntity;
import ru.mediasoft.shop.persistence.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Profile("!local")
@ConditionalOnExpression("${app.scheduling.enabled} && ${app.scheduling.optimization.enabled} == false")
public class SimpleScheduler {

    private final ProductRepository productRepository;

    @Value("${app.scheduling.priceIncreasePercentage}")
    private Double priceIncreasePercentage;

    @TimeMeter
    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    public void scheduleProductPrice(){
        final List<ProductEntity> productList = productRepository.findAll();

        productList.forEach(
                productEntity -> {
                    BigDecimal newPrice = productEntity.getPrice().multiply(BigDecimal.valueOf(priceIncreasePercentage));
                    productEntity.setPrice(newPrice);
                }
        );

        productRepository.saveAll(productList);
    }
}
