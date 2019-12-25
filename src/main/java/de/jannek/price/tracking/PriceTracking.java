package de.jannek.price.tracking;

import de.jannek.price.tracking.sql.Database;
import de.jannek.price.tracking.sql.DatabaseConfiguration;
import de.jannek.price.tracking.sql.entities.TablePriceTracking;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingSite;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingTackedProduct;
import io.ebean.EbeanServer;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Getter
    private EbeanServer sqlServer;

    public void run() {
        DOMConfigurator.configure("log4j.xml");
        connectToDatabase();

        if (false) {

        }

        final List<TablePriceTrackingSite> sites =
                sqlServer.find(TablePriceTrackingSite.class).findList();

        final List<TablePriceTrackingTackedProduct> products =
                sqlServer.find(TablePriceTrackingTackedProduct.class).findList();


        while (true) {

            for (final TablePriceTrackingTackedProduct product : products) {
                try {
                    logger.info(String.format("Scanning content of product %d (%s)", product.getId(), product.getUrl()));
                    final String content = new WebRequest().getContent(product.getUrl());
                    logger.info(String.format("Done! length: %d (%s...)", content.length(), content.substring(0, 20)));

                    final Matcher matcher = Pattern.compile(sites.get(0).getPriceRegex()).matcher(content);
                    final double price;

                    if (matcher.find() || matcher.matches()) {
                        String group = matcher.group(1);
                        price = Double.parseDouble(group);
                    } else price = 0;

                    logger.info(String.format("Price: %f", price));


                    final TablePriceTracking tablePriceTracking = new TablePriceTracking(product.getId(), price);
                    sqlServer.save(tablePriceTracking);

                    logger.info(String.format("Saved to database! ID: %d", tablePriceTracking.getId()));

                } catch (Exception e) {
                    System.out.println(String.format("Error while trying to get price from product %d", product.getId()));
                    e.printStackTrace();
                }
            }


            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ignored) {
            }
        }

    }

    private void connectToDatabase() {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

        this.sqlServer = new Database().createServer(databaseConfiguration);
    }

}
