package de.jannek.price.tracking.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfiguration {

    private String hostname;
    private int port;
    private String database;

    private String username;
    private String password;

    private int maxConnections;

}
