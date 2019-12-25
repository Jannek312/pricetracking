package de.jannek.price.tracking.sql.entities;

import io.ebean.annotation.NotNull;
import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
public class TablePriceTracking extends BaseModel {

    @NotNull
    private long productId;

    @NotNull
    private double price;


}
