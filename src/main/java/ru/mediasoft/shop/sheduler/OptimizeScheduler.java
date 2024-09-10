package ru.mediasoft.shop.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.mediasoft.shop.annotation.TimeMeter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Profile("!local")
@ConditionalOnProperty(name = {"app.scheduling.enabled", "app.scheduling.optimization.enabled"}, havingValue = "true")
public class OptimizeScheduler {
    @Value("${app.scheduling.priceIncreasePercentage}")
    private Double priceIncreasePercentage;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${app.scheduling.optimization.exclusive-lock}")
    private boolean exclusiveLock;

    @TimeMeter
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    public void scheduleProductPrice() throws SQLException {
        final String selectSql = "SELECT id, price FROM t_product";
        final String updateSql = "UPDATE t_product SET price = ? WHERE id = ?";

        final int batchSize = 10000;

        try (
                Connection connection = getNewConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectSql,
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                FileWriter fileWriter = new FileWriter("data.txt")
        ) {
            connection.setAutoCommit(false);

            if (exclusiveLock) {
                try (PreparedStatement lockStatement = connection.prepareStatement("LOCK TABLE t_product IN EXCLUSIVE MODE")) {
                    lockStatement.execute();
                }
            }

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                int updatedRecords = 0;
                while (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    BigDecimal price = resultSet.getBigDecimal("price");

                    BigDecimal newPrice = price.multiply(BigDecimal.valueOf(priceIncreasePercentage));
                    updateStatement.setBigDecimal(1, newPrice);
                    updateStatement.setObject(2, id);
                    updateStatement.addBatch();

                    updatedRecords++;
                    if (updatedRecords % batchSize == 0) {
                        updateStatement.executeBatch();
                        updateStatement.clearBatch();
                    }
                    fileWriter.write(id + " " + newPrice + "\n");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}