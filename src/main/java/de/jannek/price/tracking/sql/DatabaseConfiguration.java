package de.jannek.price.tracking.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfiguration {

    private String url;

    private String username;
    private String password;

    private int maxConnections;

}
