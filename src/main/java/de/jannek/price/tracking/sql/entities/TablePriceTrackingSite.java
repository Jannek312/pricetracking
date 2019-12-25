package de.jannek.price.tracking.sql.entities;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:08
 */

@Entity
@Table(name = "price_tracking_site")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TablePriceTrackingSite extends BaseModel {

    @NotNull
    private String name;

    @NotNull
    private String urlRegex;

    @NotNull
    private String priceRegex;

}
