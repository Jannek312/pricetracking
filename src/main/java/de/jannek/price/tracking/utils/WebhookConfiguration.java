package de.jannek.price.tracking.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: Jannek Behrens
 * Timestamp: 27/12/2019 18:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebhookConfiguration {

    private boolean enabled;
    private String url;
    private String contentError;
    private String contentPriceUp;
    private String contentPriceDown;
    private String contentPriceNew;

}
