package de.jannek.price.tracking.sql;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;

import static de.jannek.price.tracking.PriceTracking.INIT_DATA;

public class Database {

    private EbeanServer database;

    public EbeanServer getServer() {
        return database;
    }

    public EbeanServer createServer(final DatabaseConfiguration databaseConfiguration) {

        final DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername(databaseConfiguration.getUsername());
        dataSourceConfig.setPassword(databaseConfiguration.getPassword());

        dataSourceConfig.setUrl(databaseConfiguration.getUrl());

        dataSourceConfig.setHeartbeatSql("SELECT 1 FROM dual");
        dataSourceConfig.setMaxConnections(databaseConfiguration.getMaxConnections());

        ServerConfig config = new ServerConfig();
        config.setDataSourceConfig(dataSourceConfig);
        config.setDdlGenerate(true);
        config.setDdlRun(INIT_DATA);
        config.setRegister(false);
        config.setDefaultServer(false);

        config.addPackage("de.jannek.price.tracking.sql.entities");

        return this.database = EbeanServerFactory.create(config);
    }
}
