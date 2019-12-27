package de.jannek.price.tracking;

import de.jannek.price.tracking.sql.Database;
import de.jannek.price.tracking.sql.DatabaseConfiguration;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingSite;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingSiteRegex;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingTackedProduct;
import de.jannek.price.tracking.utils.WebhookConfiguration;
import io.ebean.EbeanServer;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 22:02
 */
public class PriceTracking {

    public static final boolean INIT_DATA = true; //TODO remove

    private static final String PROPERTIES_FILENAME = "price-tracking.properties";

    public static void main(String[] args) {
        new PriceTracking().run();
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Getter
    private EbeanServer sqlServer;

    @Getter
    private Properties properties;

    public void run() {
        DOMConfigurator.configure("log4j.xml");

        if (!loadProperties()) {
            logger.error(String.format("Could not load properties!"));
            return;
        }

        if (!connectToDatabase()) {
            logger.error(String.format("Could not connect to database!"));
            return;
        }


        if (INIT_DATA) {

            logger.info(String.format(""));
            logger.info(String.format("Adding Site amazon.de"));
            final TablePriceTrackingSite siteAmazonDe = new TablePriceTrackingSite("Amazon.de",
                    Pattern.compile("^https://(www.)?amazon\\.de.*").pattern(),
                    null);

            final TablePriceTrackingSite siteMindfactory = new TablePriceTrackingSite("mindfactory.de",
                    Pattern.compile("^https://(www.)?mindfactory\\.de.*").pattern(),
                    null);

            final TablePriceTrackingSite siteMediamarkt = new TablePriceTrackingSite("mediamarkt.de",
                    Pattern.compile("^https://(www.)?mediamarkt\\.de.*").pattern(),
                    null);

            final TablePriceTrackingSite siteBeyerdynamic = new TablePriceTrackingSite("beyerdynamic.de",
                    Pattern.compile("^https://(www.)?beyerdynamic\\.de.*").pattern(),
                    null);


            sqlServer.save(siteAmazonDe);
            sqlServer.save(siteMindfactory);
            sqlServer.save(siteMediamarkt);
            sqlServer.save(siteBeyerdynamic);

            logger.info(String.format("Adding regex"));
            List<TablePriceTrackingSiteRegex> regexes = new ArrayList<>();
            regexes.add(new TablePriceTrackingSiteRegex(siteAmazonDe, "main", "data-asin-price=\"([0-9]+(?:\\.[0-9]{0,2}))?\""));
            regexes.add(new TablePriceTrackingSiteRegex(siteAmazonDe, "used", Pattern.compile("<span\\sclass='a-color-price'>([0-9]+(?:\\,[0-9]{0,2}))?").pattern()));
            regexes.add(new TablePriceTrackingSiteRegex(siteMindfactory, "main", "'Artikelpreis':([0-9]+(?:\\.[0-9]{0,2}))?,")); //'Artikelpreis':225.61,
            regexes.add(new TablePriceTrackingSiteRegex(siteMediamarkt, "main", Pattern.compile("\\{\"currency\":\"EUR\",\"price\":([0-9]+(?:\\.[0-9]{0,2})?),\"").pattern())); //{"currency":"EUR","price":59.99,"
            regexes.add(new TablePriceTrackingSiteRegex(siteBeyerdynamic, "main", Pattern.compile("<meta\\sproperty=\"product:price:amount\"\\scontent=\"([0-9]+(?:\\.[0-9]{0,2})?)\"/><script\\ssrc=\".{5,400}\"\\scrossorigin=\"anonymous\"></script>").pattern())); //<meta property="product:price:amount" content="299.00"/><script src="https://polyfill.io/v3/polyfill.min.js?features=default%2CArray.prototype.includes%2CPromise" crossorigin="anonymous"></script>
            regexes.forEach(sqlServer::save);


            logger.info(String.format("Adding products"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.amazon.de/gp/product/B07G9J35CQ/"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.amazon.de/gp/product/B06XP9N2VP/"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.amazon.de/gp/product/B07GRTYDDV/"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.amazon.de/dp/B001D7UYBO//"));

            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.mindfactory.de/product_info.php/be-quiet--Dark-Base-Pro-900-Rev--2-gedaemmt-mit-Sichtfenster-Big-Tower-_1256121.html"));

            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.mediamarkt.de/de/product/_htc-vive-pro-full-kit-2436298.html"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.mediamarkt.de/de/product/_call-of-duty-modern-warfare-action-xbox-one-2564145.html"));

            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.beyerdynamic.de/lagoon-anc-explorer.html"));
            sqlServer.save(new TablePriceTrackingTackedProduct("https://www.beyerdynamic.de/amiron-wireless-copper.html?cid=mm_produkt"));
        }

        final WebhookConfiguration webhookConfiguration = new WebhookConfiguration(
                Boolean.parseBoolean(properties.getProperty("webhook.enabled")),
                properties.getProperty("webhook.url"),
                properties.getProperty("webhook.content.error"),
                properties.getProperty("webhook.content.price.up"),
                properties.getProperty("webhook.content.price.down"),
                properties.getProperty("webhook.content.price.new"));

        new Thread(new PriceTracker(sqlServer, false, 120, true, webhookConfiguration), "Price_Tracker").start();


    }

    private boolean loadProperties() {
        final Properties properties = new Properties();
        try {
            final File file = new File(PROPERTIES_FILENAME);
            if (file.createNewFile()) {

                //copy default properties

                final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
                final byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                final OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);

                logger.warn(String.format("Please set up %s file!", PROPERTIES_FILENAME));
                return false;
            } else {
                properties.load(new FileInputStream(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.properties = properties;
        return true;
    }

    private boolean connectToDatabase() {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                properties.getProperty("database.jdbc.url"),
                properties.getProperty("database.username"),
                properties.getProperty("database.password"),
                Integer.parseInt(properties.getProperty("database.max.connections")));

        try {
            this.sqlServer = new Database().createServer(databaseConfiguration);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}
