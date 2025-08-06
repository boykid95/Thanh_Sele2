package vn.agest.selenide.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import vn.agest.selenide.common.utilities.helpers.ConfigFileReader;
import vn.agest.selenide.common.utilities.other.Log;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.pageObjects.HomePage;
import vn.agest.selenide.pageObjects.LoginPage;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {
    public String testCaseName;
    public HomePage homePage = new HomePage();
    public LoginPage loginPage = new LoginPage();
    public SoftAssert softAssert = new SoftAssert();

    @Parameters("browser")
    @BeforeClass
    public void setUp(String browser) {
        try {
            Configuration.browser = browser;
            Configuration.browserSize = "1920x1080";
            Configuration.timeout = 5000;

            open(ConfigFileReader.getUrlFromPageType(PageType.HOME_PAGE));

            testCaseName = this.getClass().getSimpleName();
        } catch (Exception e) {
            Log.error("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @AfterClass
    public void tearDown() {
        try {
            Selenide.sleep(2000);
            closeWebDriver();
        } catch (Exception e) {
            Log.error("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
