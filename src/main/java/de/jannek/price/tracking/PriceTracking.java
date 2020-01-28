package de.jannek.price.tracking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import de.jannek.price.tracking.sql.Database;
import de.jannek.price.tracking.sql.DatabaseConfiguration;
import de.jannek.price.tracking.utils.WebhookConfiguration;
import io.ebean.EbeanServer;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 22:02
 */
public class PriceTracking {

    public static boolean INIT_DATA = false; //TODO remove

    private static final String PROPERTIES_FILENAME = "price-tracking.properties";

    public static void main(String[] args) {
        new PriceTracking().run(args);
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Getter
    private EbeanServer sqlServer;

    @Getter
    private Properties properties;

    private void run(String[] args) {
        DOMConfigurator.configure("log4j.xml");

        if (!loadProperties()) {
            logger.error("Could not load properties!");
            return;
        }

        if (args != null && args.length == 1) {
            final String arg0 = args[0].toLowerCase();
            INIT_DATA = arg0.equals("true");
        } else {
            INIT_DATA = Boolean.parseBoolean(properties.getProperty("init"));
        }


        if (!connectToDatabase()) {
            logger.error("Could not connect to database!");
            return;
        }


        if (INIT_DATA) {
            new InitData(sqlServer).initial();
        }

        final WebhookConfiguration webhookConfiguration = new WebhookConfiguration(
                Boolean.parseBoolean(properties.getProperty("webhook.enabled")),
                properties.getProperty("webhook.url"),
                properties.getProperty("webhook.content.error"),
                properties.getProperty("webhook.content.price.up"),
                properties.getProperty("webhook.content.price.down"),
                properties.getProperty("webhook.content.price.new"));

        final PriceTracker priceTracker = new PriceTracker(sqlServer,
                Boolean.parseBoolean(properties.getProperty("tracking.save.raw.data")),
                Integer.parseInt(properties.getProperty("tracking.interval")),
                Boolean.parseBoolean(properties.getProperty("tracking.only.save.changes")),
                webhookConfiguration);

        new Thread(priceTracker, "Price_Tracker").start();


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
