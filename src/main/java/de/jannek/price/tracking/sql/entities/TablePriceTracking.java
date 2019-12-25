package de.jannek.price.tracking.sql.entities;

import io.ebean.Model;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:07
 */
@Entity
@Table(name = "price_tracking")
@Data
public class TablePriceTracking extends Model {

    @Id
    private int trackingId;
    private double price;
    private Timestamp createdAt;


}
