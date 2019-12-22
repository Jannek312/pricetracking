package de.jannek.price.tracking.test;

import de.jannek.price.tracking.sql.TablePriceTracking;
import io.ebean.DB;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 21/12/2019 21:49
 */
public class TablePriceTrackingTest {

    @Test
    public void insertFindDelete() {
        final TablePriceTracking priceTracking = new TablePriceTracking();
        priceTracking.setPrice(329.65);
        DB.save(priceTracking);

        final TablePriceTracking foundPriceTracking = DB.find(TablePriceTracking.class, 1);

        DB.delete(foundPriceTracking);

    }

}