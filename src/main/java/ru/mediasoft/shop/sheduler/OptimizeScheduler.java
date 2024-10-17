package ru.mediasoft.shop.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.mediasoft.shop.annotation.TimeMeter;
import ru.mediasoft.shop.configuration.properties.SchedulerConfig;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Profile("!local")
@ConditionalOnProperty(name = {"app.scheduling.enabled", "app.scheduling.optimization.enabled"}, havingValue = "true")
public class OptimizeScheduler {

    private final DataSource dataSource;
    private final SchedulerConfig schedulerConfig;
    private static final String SELECT_SQL = "SELECT * FROM t_product FOR UPDATE";
    private static final String UPDATE_SQL = "UPDATE t_product SET price = ? WHERE id = ?";

    @TimeMeter
    @Scheduled(fixedDelayString = "#{@schedulerConfig.getPeriod()}")
    public void scheduleProductPrice() {
        final int batchSize = 10000;

        try (
                Connection connection = getNewConnection();
                PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL);
                FileWriter fileWriter = new FileWriter("data.txt")
        ) {
            connection.setAutoCommit(false);

            if (schedulerConfig.isExclusiveLock()) {
                connection.createStatement().executeQuery("LOCK TABLE t_product IN EXCLUSIVE MODE");
            }

            Statement selectStatement = connection.createStatement();

            try (ResultSet resultSet = selectStatement.executeQuery(SELECT_SQL)) {
                int updatedRecords = 0;
                while (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    BigDecimal price = resultSet.getBigDecimal("price");

                    BigDecimal newPrice = price.multiply(BigDecimal.valueOf(schedulerConfig.getPriceIncreasePercentage()));
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
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getNewConnection() throws SQLException {
        return dataSource.getConnection();
    }
}