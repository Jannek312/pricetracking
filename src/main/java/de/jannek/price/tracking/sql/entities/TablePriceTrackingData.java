package de.jannek.price.tracking.sql.entities;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:07
 */

@Entity
@Table(name = "price_tracking_data")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TablePriceTrackingData extends BaseModel {

    @NotNull
    private long productId;

    @NotNull
    private String type;

    @NotNull
    private double price;

}
