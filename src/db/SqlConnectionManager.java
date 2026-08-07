package db;

import settings.DbSettingsReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnectionManager implements IDBConnectionManager {
    private final DbSettingsReader settings;
    private Connection connection;

    public SqlConnectionManager(DbSettingsReader settings) {
        this.settings = settings;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Если подключение закрыто или его нет, создаем новое
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    settings.getUrl(),
                    settings.getUser(),
                    settings.getPassword()
            );
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии подключения: " + e.getMessage());
        }
    }
}