package ru.mediasoft.shop.sheduler;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mediasoft.shop.annotation.TimeMeter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Profile("!local")
@EnableScheduling
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('super')}")
@Slf4j
public class SuperOptimizedScheduler {

    private static final String QUERY = "UPDATE t_product SET price = price * (1 + ?/100) RETURNING *";

    private final BigDecimal priceIncreasePercentage;

    private final EntityManagerFactory entityManagerFactory;

    public SuperOptimizedScheduler(
            @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
            BigDecimal priceIncreasePercentage,
            EntityManagerFactory entityManagerFactory
    ) {
        this.priceIncreasePercentage = priceIncreasePercentage;
        this.entityManagerFactory = entityManagerFactory;
    }

    @TimeMeter
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    public void increaseProductPrice() {
        log.info("Start SUPER OptimizedProductPriceScheduler");

        final Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        try (session) {
            session.doWork(connection -> {
                try (
                        final BufferedWriter fileWriter = new BufferedWriter(new FileWriter("results.txt"));
                        connection
                ) {
                    connection.setAutoCommit(false);
                    final PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
                    preparedStatement.setBigDecimal(1, priceIncreasePercentage);

                    final ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        fileWriter.write(buildString(resultSet));
                    }

                    connection.commit();
                } catch (Exception e) {
                    connection.rollback();
                    throw new RuntimeException(e);
                }
            });
        }

        log.info("End Super OptimizedProductPriceScheduler");
    }

    private String buildString(final ResultSet resultSet) throws SQLException {
        return resultSet.getString("id") +
                " " +
                resultSet.getString("name") +
                " " +
                resultSet.getString("description") +
                " " +
                resultSet.getString("price") +
                " " +
                resultSet.getString("article");
    }
}