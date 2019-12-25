package de.jannek.price.tracking.sql.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:07
 */
@Entity
@Table(name = "price_tracking")
@Data
public class TablePriceTracking extends BaseModel {

    private double price;


}
