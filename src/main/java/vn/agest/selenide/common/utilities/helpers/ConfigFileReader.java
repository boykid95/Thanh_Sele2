package vn.agest.selenide.common.utilities.helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenide.enums.PageType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigFileReader {
    private static final Logger logger = LogManager.getLogger(ConfigFileReader.class);

    private static final Properties configProps = new Properties();
    private static final Properties credentialProps = new Properties();

    static {
        loadProperties();
    }

    @Step("Load config.properties and credentials.properties files")
    private static void loadProperties() {
        loadFile("config.properties", configProps);
        loadFile("credentials.properties", credentialProps);
    }

    private static void loadFile(String fileName, Properties targetProps) {
        try (InputStream inputStream = ConfigFileReader.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException(fileName + " not found in resources folder");
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                targetProps.load(reader);
            }
        } catch (Exception e) {
            logger.error("Failed to load " + fileName, e);
            Allure.step("Error loading " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Error loading " + fileName, e);
        }
    }

    @Step("Get value from config.properties: {key}")
    public static String getProperty(String key) {
        String value = configProps.getProperty(key);
        if (value == null) {
            logger.error("Config property key not found: {}", key);
            throw new IllegalArgumentException("Config property key not found: " + key);
        }
        return value.trim();
    }

    @Step("Get value from credentials.properties: {key}")
    public static String getCredentialProperty(String key) {
        String value = credentialProps.getProperty(key);
        if (value == null) {
            logger.error("Credential property key not found: {}", key);
            throw new IllegalArgumentException("Credential property key not found: " + key);
        }
        return value.trim();
    }

    @Step("Get Username")
    public static String getUsername() {
        return getCredentialProperty("username");
    }

    @Step("Get Password")
    public static String getPassword() {
        return getCredentialProperty("password");
    }

    @Step("Get Browser")
    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getBaseUrl() {
        return getProperty("homePageUrl");
    }

    @Step("Get URL from PageType: {pageType}")
    public static String getUrlFromPageType(PageType pageType) {
        String url = configProps.getProperty(pageType.getUrlKey());
        if (url == null) {
            logger.error("URL key not found for PageType: {}", pageType.name());
            throw new IllegalArgumentException("URL key not found for PageType: " + pageType.name());
        }
        return url.trim();
    }
}
