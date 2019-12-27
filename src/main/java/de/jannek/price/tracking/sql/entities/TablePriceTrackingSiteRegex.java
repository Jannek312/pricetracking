package de.jannek.price.tracking.sql.entities;

import io.ebean.annotation.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 27/12/2019 15:10
 */

@Entity
@Table(name = "price_tracking_site_regex")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TablePriceTrackingSiteRegex extends BaseModel {

    public TablePriceTrackingSiteRegex(TablePriceTrackingSite site, String type, String priceRegex) {
        this.site = site;
        this.type = type;
        this.priceRegex = priceRegex;
    }

    @ManyToOne
    private TablePriceTrackingSite site;

    @NotNull
    private String type;

    @NotNull
    private String priceRegex;

}
