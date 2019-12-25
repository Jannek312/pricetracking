package de.jannek.price.tracking.sql.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:08
 */
@Entity
@Table(name = "price_tracking_tracked_product")
public class TablePriceTrackingTackedProduct extends BaseModel {

    private String url;

}
