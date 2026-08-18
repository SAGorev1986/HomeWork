package factory;

import db.IDBConnectionManager;
import db.SqlConnectionManager;
import settings.DbSettingsReader;

public class DBFactory {

    public IDBConnectionManager getConnectionManager(String dbType) {
        DbSettingsReader settings = new DbSettingsReader();

        if ("sql".equalsIgnoreCase(dbType) || "postgresql".equalsIgnoreCase(dbType)) {
            return SqlConnectionManager.getInstance(settings);
        }

        throw new IllegalArgumentException("Неподдерживаемый тип БД: " + dbType);
    }
}