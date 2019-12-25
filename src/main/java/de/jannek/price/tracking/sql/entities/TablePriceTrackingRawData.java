package de.jannek.price.tracking.sql.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:08
 */
@Entity
@Table(name = "price_tracking_raw_data")
public class TablePriceTrackingRawData extends BaseModel {

    private String data;

}
