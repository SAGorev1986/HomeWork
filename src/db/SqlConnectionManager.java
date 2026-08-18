package db;

import settings.DbSettingsReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlConnectionManager implements IDBConnectionManager {

    private static volatile SqlConnectionManager instance;

    private final HikariDataSource dataSource;

    private SqlConnectionManager(DbSettingsReader settings) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(settings.getUrl());
        config.setUsername(settings.getUser());
        config.setPassword(settings.getPassword());

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
    }

    // Singleton
    public static SqlConnectionManager getInstance(DbSettingsReader settings) {
        if (instance == null) {
            synchronized (SqlConnectionManager.class) {
                if (instance == null) {
                    instance = new SqlConnectionManager(settings);
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close(); // Корректно закрывает ВСЕ физические соединения
        }
    }
}