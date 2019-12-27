package de.jannek.price.tracking.sql.entities;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 19/12/2019 23:08
 */

@Entity
@Table(name = "price_tracking_data_raw")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TablePriceTrackingDataRaw extends BaseModel {

    @OneToOne(mappedBy = "rawData")
    @JoinColumn(name = "id")
    private TablePriceTrackingData data;

    @Lob @NotNull
    private byte[] rawData;

}
