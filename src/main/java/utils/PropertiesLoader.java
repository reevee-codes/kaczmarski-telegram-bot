package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties load(String fileName) {
        Properties props = new Properties();
        try (InputStream input = PropertiesLoader.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException(fileName + " file not found");
            }
            props.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }
    }
}

