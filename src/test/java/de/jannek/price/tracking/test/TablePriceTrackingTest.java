package de.jannek.price.tracking.test;

import de.jannek.price.tracking.sql.entities.TablePriceTracking;
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

        final TablePriceTracking foundPriceTracking = DB.find(TablePriceTracking.class)
                .where().eq("price", 329.65).findOne();

        DB.delete(foundPriceTracking);

    }

}