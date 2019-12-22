package de.jannek.price.tracking;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 22:02
 */
public class PriceTracking {

    public static void main(String[] args) {
        new PriceTracking().run();
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    public void run() {
        DOMConfigurator.configure("log4j.xml");

        connectToDatabase();
    }

    private void connectToDatabase() {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/price_tracking");
        config.setUsername("root");


        final HikariDataSource dataSource = new HikariDataSource(config);

        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT VERSION() AS VERSION;");
            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String version = resultSet.getString("VERSION");
                if (version != null) {
                    version = version.replace("Maria", "Maria-and-Joseph");
                }

                logger.info(String.format("Connected to database version %s", version));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

}
