package tables;

import db.IDBConnectionManager;
import factory.DBFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsBaseTable {
    protected final IDBConnectionManager idbConnectionManager;
    protected final String tableName;

    public AbsBaseTable(String tableName) throws SQLException {
        this.idbConnectionManager = new DBFactory().getConnectionManager("sql");
        this.tableName = tableName;
    }

    // Метод для получения списка записей
    public List<Map<String, String>> list(String... columnsName) throws SQLException {
        List<Map<String, String>> results = new ArrayList<>();

        String columns = columnsName.length > 0 ? String.join(", ", columnsName) : "*";
        String sql = String.format("SELECT %s FROM %s", columns, tableName);

        ResultSet result = idbConnectionManager.getConnection().createStatement().executeQuery(sql);

        while (result.next()) {
            Map<String, String> tableData = new HashMap<>();
            ResultSetMetaData metaData = result.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                tableData.put(columnName, result.getString(i));
            }
            results.add(tableData);
        }

        return results;
    }

    // Метод для вставки записи
    public void insert(Map<String, String> data) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<String> valueList = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(entry.getKey());
            values.append("?");
            valueList.add(entry.getValue());
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns.toString(), values.toString());

        PreparedStatement pstmt = idbConnectionManager.getConnection().prepareStatement(sql);
        for (int i = 0; i < valueList.size(); i++) {
            pstmt.setString(i + 1, valueList.get(i));
        }
        pstmt.executeUpdate();
    }

    // Универсальный метод обновления (возвращает true, если запись найдена и обновлена)
    public boolean update(int id, Map<String, String> data) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        List<String> valueList = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
            valueList.add(entry.getValue());
        }

        String sql = String.format("UPDATE %s SET %s WHERE id = ?",
                tableName, setClause.toString());

        PreparedStatement pstmt = idbConnectionManager.getConnection().prepareStatement(sql);
        for (int i = 0; i < valueList.size(); i++) {
            pstmt.setString(i + 1, valueList.get(i));
        }
        pstmt.setInt(valueList.size() + 1, id);

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }

    // Универсальный метод удаления (возвращает true, если запись найдена и удалена)
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        PreparedStatement pstmt = idbConnectionManager.getConnection().prepareStatement(sql);
        pstmt.setInt(1, id);

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }


    // Метод для удаления таблицы
    public void deleteTable() throws SQLException {
        String sql = String.format("DROP TABLE %s", tableName);
        idbConnectionManager.getConnection().createStatement().execute(sql);
    }

    // Закрытие подключения
    public void close() {
        idbConnectionManager.close();
    }
}