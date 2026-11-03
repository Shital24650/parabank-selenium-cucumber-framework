package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    // A -Dkey=value on the command line overrides the file.
    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }
}
