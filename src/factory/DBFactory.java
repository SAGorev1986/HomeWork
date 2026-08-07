package factory;

import db.IDBConnectionManager;
import db.SqlConnectionManager;
import settings.DbSettingsReader;

public class DBFactory {

    // Метод возвращает нужный менеджер в зависимости от типа БД
    public IDBConnectionManager getConnectionManager(String dbType) {
        DbSettingsReader settings = new DbSettingsReader();

        if ("sql".equalsIgnoreCase(dbType) || "postgresql".equalsIgnoreCase(dbType)) {
            return new SqlConnectionManager(settings);
        }

        // Если в будущем добавим MySQL, будет здесь
        throw new IllegalArgumentException("Неподдерживаемый тип БД: " + dbType);
    }
}