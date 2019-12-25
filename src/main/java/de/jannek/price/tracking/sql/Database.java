package de.jannek.price.tracking.sql;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;

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

        dataSourceConfig.setUrl(String.format("jdbc:mysql://%s:%d/%s",
                databaseConfiguration.getHostname(),
                databaseConfiguration.getPort(),
                databaseConfiguration.getDatabase()));

        dataSourceConfig.setHeartbeatSql("SELECT 1 FROM dual");
        dataSourceConfig.setMaxConnections(databaseConfiguration.getMaxConnections());

        ServerConfig config = new ServerConfig();
        config.setName(String.format("%s-%s", databaseConfiguration.getHostname(), databaseConfiguration.getDatabase()));
        config.setDataSourceConfig(dataSourceConfig);
        config.setDdlGenerate(true);
        config.setDdlRun(false);
        config.setRegister(false);
        config.setDefaultServer(false);

        config.addPackage("de.jannek.price.tracking.sql.entities");

        return this.database = EbeanServerFactory.create(config);
    }
}
