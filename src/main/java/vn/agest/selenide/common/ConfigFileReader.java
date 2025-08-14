package vn.agest.selenide.common;

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
    private static final Properties pageTitlesProps = new Properties();

    static {
        loadFile("config.properties", configProps);
        loadFile("credentials.properties", credentialProps);
        loadFile("pageTitles.properties", pageTitlesProps);
    }

    @Step("Load file: {fileName}")
    private static void loadFile(String fileName, Properties targetProps) {
        try (InputStream inputStream = ConfigFileReader.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException(fileName + " not found in resources folder");
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                targetProps.load(reader);
                Allure.step("Loaded file successfully: " + fileName);
            }
        } catch (Exception e) {
            logger.error("Failed to load " + fileName, e);
            Allure.step("Error loading " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Error loading " + fileName, e);
        }
    }

    @Step("Get value from config.properties: {key}")
    public static String getProperty(String key) {
        return getValue(configProps, key, "Config");
    }

    @Step("Get value from credentials.properties: {key}")
    public static String getCredentialProperty(String key) {
        return getValue(credentialProps, key, "Credential");
    }

    @Step("Get Username from credentials.properties")
    public static String getUsername() {
        return getCredentialProperty("username");
    }

    @Step("Get Password from credentials.properties")
    public static String getPassword() {
        return getCredentialProperty("password");
    }

    @Step("Get Browser from config.properties")
    public static String getBrowser() {
        return getProperty("browser");
    }

    @Step("Get Base URL from config.properties")
    public static String getBaseUrl() {
        return getProperty("homePageUrl");
    }

    @Step("Get URL from PageType: {pageType}")
    public static String getUrlFromPageType(PageType pageType) {
        return getValue(configProps, pageType.getUrlKey(), "Config");
    }

    @Step("Get Title from PageType: {pageType}")
    public static String getTitleFromPageType(PageType pageType) {
        return getValue(pageTitlesProps, pageType.getTitleKey(), "PageTitles");
    }

    @Step("Get value from {typeName} properties with key: {key}")
    private static String getValue(Properties props, String key, String typeName) {
        String value = props.getProperty(key);
        if (value == null) {
            logger.error("❌ {} property key not found: {}", typeName, key);
            Allure.step("❌ " + typeName + " property key not found: " + key);
            throw new IllegalArgumentException(typeName + " property key not found: " + key);
        }
        Allure.step("✅ Found key [" + key + "] in " + typeName + " with value: " + value.trim());
        return value.trim();
    }
}
