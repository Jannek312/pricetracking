package de.jannek.price.tracking.test;

import de.jannek.price.tracking.sql.entities.TablePriceTrackingData;
import io.ebean.DB;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 21/12/2019 21:49
 */
public class TablePriceTrackingDataTest {

    @Test
    public void insertFindDelete() {
        final TablePriceTrackingData priceTracking = new TablePriceTrackingData();
        priceTracking.setPrice(329.65);
        priceTracking.setType("main");
        DB.save(priceTracking);

        final TablePriceTrackingData foundPriceTracking = DB.find(TablePriceTrackingData.class)
                .where().eq("price", 329.65).findOne();

        DB.delete(foundPriceTracking);

    }

}