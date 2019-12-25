package de.jannek.price.tracking;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.MalformedURLException;
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

        String[] urls = new String[]{
                "https://www.amazon.de/gp/product/B07G9J35CQ",
                "https://www.amazon.de/gp/product/B07GRTYDDV/",
                "https://www.amazon.de/gp/product/B06XP9N2VP"
        };

        final PriceTracking priceTracking = new PriceTracking();
        for (String url : urls) {
            priceTracking.run(url);
        }
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    public void run(final String url) {
        DOMConfigurator.configure("log4j.xml");

        try {
            System.out.println(String.format("Scanning site %s...", url));
            final String content = new WebRequest().getContent(url);
            System.out.println("Done!");
            System.out.println(String.format("Contnet: %s (%d)", content.substring(0, 10), content.length()));

            String price = new PriceParser().getPrice(content);
            System.out.println(String.format("Price: " + price));
            System.out.println();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //connectToDatabase();
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
