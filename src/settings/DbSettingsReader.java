package settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbSettingsReader {
    private final Properties properties;

    public DbSettingsReader() {
        properties = new Properties();
        try {
            // Прямой путь к файлу — гарантированно работает
            String projectPath = System.getProperty("user.dir");
            String filePath = projectPath + "/src/resources/db.properties";

            System.out.println("Ищем файл по пути: " + filePath);

            try (FileInputStream input = new FileInputStream(filePath)) {
                properties.load(input);
                System.out.println("✅ Файл db.properties успешно загружен!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения db.properties: " + e.getMessage());
        }
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUser() {
        return properties.getProperty("db.user");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }

    public String getDriver() {
        return properties.getProperty("db.driver");
    }
}