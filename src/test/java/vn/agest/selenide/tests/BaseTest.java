package vn.agest.selenide.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import vn.agest.selenide.common.ConfigFileReader;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.pageObjects.HomePage;
import vn.agest.selenide.pageObjects.LoginPage;
import vn.agest.selenide.pageObjects.MyAccountPage;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.*;

@Log4j
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
            homePage.closePopupIfPresent();

            testCaseName = this.getClass().getSimpleName();
        } catch (Exception e) {
            log.error("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @AfterMethod(alwaysRun = true)
    public void logoutAfterEachTest() {
        try {
            log.info("🔄 Attempting to logout after test...");
            homePage.logOut();
        } catch (Exception e) {
            log.info("⚡ Skip logout, probably already logged out.");
        }
    }

    @AfterClass
    public void tearDown() {
        try {
            Selenide.sleep(2000);
            closeWebDriver();
        } catch (Exception e) {
            log.error("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
