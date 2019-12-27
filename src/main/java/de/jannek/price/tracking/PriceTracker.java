package de.jannek.price.tracking;

import de.jannek.price.tracking.sql.entities.TablePriceTrackingData;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingDataRaw;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingSite;
import de.jannek.price.tracking.sql.entities.TablePriceTrackingTackedProduct;
import io.ebean.EbeanServer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 27/12/2019 00:25
 */
public class PriceTracker implements Runnable {

    private static final Logger logger = LogManager.getLogger(PriceTracker.class);

    private EbeanServer sqlServer;
    private final boolean saveRaw;
    private final int interval;
    private final boolean onlySaveChanges;

    public PriceTracker(final EbeanServer sqlServer, final boolean saveRaw, final int interval, final boolean onlySaveChanges) {
        this.sqlServer = sqlServer;
        this.saveRaw = saveRaw;
        this.interval = interval;
        this.onlySaveChanges = onlySaveChanges;
    }

    @Override
    public void run() {
        while (true) {

            logger.info("New round states now...");
            scan();
            logger.info("All products were scanned!");

            if (PriceTracking.INIT_DATA) {
                System.exit(1);
            }

            try {
                Thread.sleep(1000 * interval);
            } catch (InterruptedException ignored) {
            }
        }
    }


    private void scan() {
        final List<TablePriceTrackingTackedProduct> products =
                sqlServer.find(TablePriceTrackingTackedProduct.class).findList();

        products.forEach(product -> {
            try {
                logger.info(String.format("Scanning content of product %d (%s)", product.getId(), product.getUrl()));


                final List<TablePriceTrackingSite> sites =
                        sqlServer.find(TablePriceTrackingSite.class).findList()
                                .stream().filter(site -> product.getUrl().matches(site.getUrlRegex()))
                                .collect(Collectors.toList());

                logger.info(String.format("Found %d sites! (%s)", sites.size(), sites.stream().map(TablePriceTrackingSite::getName).collect(Collectors.joining())));

                if (sites.size() == 0) {
                    return;
                }

                final String content = new WebRequest().getContent(product.getUrl());
                logger.info(String.format("Done! length: %d (%s...)", content.length(), content.substring(0, 20)));

                sites.forEach(site -> site.getTablePriceTrackingSiteRegexes().forEach(siteRegex -> {
                    final Matcher matcher = Pattern.compile(siteRegex.getPriceRegex()).matcher(content);
                    final Double price;
                    if (matcher.find() || matcher.matches()) {
                        price = Double.parseDouble(matcher.group(1).replace(",", "."));
                    } else {
                        logger.info(String.format("No price found!"));
                        return;
                    }


                    final TablePriceTrackingData lastData = findLastData(product.getId(), siteRegex.getType());
                    final boolean priceChanged = lastData == null || lastData.getPrice() != price;
                    if (saveRaw && priceChanged) {
                        final TablePriceTrackingDataRaw rawData = new TablePriceTrackingDataRaw(product.getId(), content);
                        sqlServer.save(rawData);
                    }

                    if (priceChanged) {
                        logger.info(String.format("Price %s changed from %.2f to %.2f for product %d", siteRegex.getType(),
                                lastData == null ? 0 : lastData.getPrice(),
                                price, product.getId()));
                    } else {
                        logger.info(String.format("Product %s %d has the same price (%.2f)!", siteRegex.getType(), product.getId(), price));
                    }

                    if (onlySaveChanges && !priceChanged) {
                        return;
                    }

                    final TablePriceTrackingData trackingData = new TablePriceTrackingData(product.getId(), siteRegex.getType(), price); //TODO
                    sqlServer.save(trackingData);
                    logger.info(String.format("Saved %s price of product %d to database! ID: %d", siteRegex.getType(), product.getId(), trackingData.getId()));


                }));
                logger.info(String.format("Finished product %d!\n", product.getId()));

            } catch (Exception e) {
                System.out.println(String.format("Error while trying to get price from product %d", product.getId()));
                e.printStackTrace();
            }
        });
    }

    private TablePriceTrackingData findLastData(final long productId, final String type) {
        final TablePriceTrackingData lastData = sqlServer.find(TablePriceTrackingData.class).
                where().eq("product_id", productId)
                .eq("type", type)
                .order().desc("when_created")
                .setMaxRows(1).findOne();
        return lastData;
    }

}
