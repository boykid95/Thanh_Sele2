package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    @BeforeClass
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://demo.testarchitect.com";
        Configuration.browserSize = "1920x1080";
    }
}