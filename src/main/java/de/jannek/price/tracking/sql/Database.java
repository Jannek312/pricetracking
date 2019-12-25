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

    public EbeanServer createServer(
            final String username,
            final String password,
            final String hostname,
            final int port,
            final String database,
            final int maxConnections) {

        final DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=yes&characterEncoding=UTF-8",
                hostname,
                port,
                database));

        dataSourceConfig.setHeartbeatSql("SELECT 1 FROM dual");
        dataSourceConfig.setMaxConnections(maxConnections);

        ServerConfig config = new ServerConfig();
        config.setName(String.format("%s-%s", hostname, database));
        config.setDataSourceConfig(dataSourceConfig);
        config.setDdlGenerate(true);
        config.setDdlRun(false);
        config.setRegister(false);
        config.setDefaultServer(false);

        config.addPackage("de.jannek.price.tracking.sql.entities");
        //config.addClass(CLASS.class);

        return this.database = EbeanServerFactory.create(config);
    }
}
