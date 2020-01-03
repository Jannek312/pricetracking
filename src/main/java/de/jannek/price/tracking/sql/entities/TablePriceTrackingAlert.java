package de.jannek.price.tracking.sql.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 03/01/2020 15:47
 */

@Entity
@Table(name = "price_tracking_alert")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TablePriceTrackingAlert extends BaseModel {

    @ManyToOne
    private TablePriceTrackingTackedProduct product;

    private double price;

    /**
     * The protocol of the notification e. g. SMS, Mail, ...
     */
    private String destinationType;
    /**
     * The E-Mail address of the client
     */
    private String destination;

}
