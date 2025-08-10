package io.github.stardew.mini;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            } else {
                properties.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getApiKey() {
        return properties.getProperty("OPENROUTER_API_KEY");
    }
}
