package tables;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalTable extends AbsBaseTable {

    public AnimalTable() throws SQLException {
        super("animals");
    }

    // Метод для добавления животного (используем прямые типы данных)
    public void addAnimal(String type, String name, int age, double weight, String color) throws SQLException {
        String sql = "INSERT INTO animals (type, name, age, weight, color) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = idbConnectionManager.getConnection().prepareStatement(sql);

        pstmt.setString(1, type);
        pstmt.setString(2, name);
        pstmt.setInt(3, age);
        pstmt.setDouble(4, weight); // <-- Вот это важно! setDouble вместо setString
        pstmt.setString(5, color);

        pstmt.executeUpdate();
    }

    // Метод для получения всех животных
    public List<Map<String, String>> getAllAnimals() throws SQLException {
        return list();
    }

    // Метод для фильтрации по типу
    public List<Map<String, String>> getAnimalsByType(String type) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE type = '%s'", tableName, type);
        return executeQuery(sql);
    }

    // Метод для обновления животного (возвращает true, если запись была обновлена)
    public boolean updateAnimal(int id, String name, int age, double weight, String color) throws SQLException {
        String sql = "UPDATE animals SET name = ?, age = ?, weight = ?, color = ? WHERE id = ?";
        PreparedStatement pstmt = idbConnectionManager.getConnection().prepareStatement(sql);

        pstmt.setString(1, name);
        pstmt.setInt(2, age);
        pstmt.setDouble(3, weight);
        pstmt.setString(4, color);
        pstmt.setInt(5, id);

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }

    // Вспомогательный метод для выполнения произвольного запроса (для фильтрации)
    private List<Map<String, String>> executeQuery(String sql) throws SQLException {
        List<Map<String, String>> results = new ArrayList<>();
        java.sql.ResultSet result = idbConnectionManager.getConnection().createStatement().executeQuery(sql);

        while (result.next()) {
            Map<String, String> tableData = new HashMap<>();
            java.sql.ResultSetMetaData metaData = result.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                tableData.put(columnName, result.getString(i));
            }
            results.add(tableData);
        }

        return results;
    }
}