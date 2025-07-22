package vn.agest.selenide.common.utilities.helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.enums.ProductCategory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PageTitlesFileReader {
    private static final Logger logger = LogManager.getLogger(PageTitlesFileReader.class);
    private static final Properties properties = new Properties();

    static {
        loadPageTitles();
    }

    @Step("Load pageTitles.properties file")
    private static void loadPageTitles() {
        try (InputStream inputStream = PageTitlesFileReader.class.getClassLoader()
                .getResourceAsStream("pageTitles.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("pageTitles.properties file not found in resources folder");
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        } catch (Exception e) {
            logger.error("Failed to load pageTitles.properties", e);
            Allure.step("Error loading pageTitles.properties: " + e.getMessage());
            throw new RuntimeException("Error loading pageTitles.properties", e);
        }
    }

    @Step("Get title for pageType {pageType}")
    public static String getTitleFromPageType(PageType pageType) {
        String title = properties.getProperty(pageType.getTitleKey());
        if (title == null) {
            logger.error("Title key not found: {}", pageType.getTitleKey());
            throw new IllegalArgumentException("Title key not found: " + pageType.getTitleKey());
        }
        return title.trim();
    }

}
