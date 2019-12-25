package de.jannek.price.tracking.test;

import de.jannek.price.tracking.sql.entities.TablePriceTrackingTackedProduct;
import io.ebean.DB;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TablePriceTrackingDataTrackedProductTest {


    @Test
    public void insertFindDelete() {

        final TablePriceTrackingTackedProduct tablePriceTrackingTackedProduct = new TablePriceTrackingTackedProduct();
        tablePriceTrackingTackedProduct.setUrl("https://www.amazon.de/gp/hjkasdhat");
        tablePriceTrackingTackedProduct.save();

        // find using an expression query
        final TablePriceTrackingTackedProduct rob =
                DB.find(TablePriceTrackingTackedProduct.class)
                        .where().istartsWith("url", "https://www.amazon.de/gp/hjkasdhat")
                        .findOne();

        assertNotNull(rob);

        // IDE Re-build to generate the QCustomer query bean
//    // find using a query bean
//    final Customer rob2 = new QCustomer()
//      .name.istartsWith("ro")
//      .findOne();
//
//    assertNotNull(rob);

        rob.delete();

        final TablePriceTrackingTackedProduct notThere = DB.find(TablePriceTrackingTackedProduct.class,
                tablePriceTrackingTackedProduct.getId());
        assertNull(notThere);
    }

}
