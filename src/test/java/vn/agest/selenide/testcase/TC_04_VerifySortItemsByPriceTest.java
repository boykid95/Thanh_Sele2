package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;
import vn.agest.selenide.common.Constants;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j
public class TC_04_VerifySortItemsByPriceTest extends BaseTest {

    @Test(description = "Verify that users can sort items by price in ascending and descending order")
    @Description("TC_04 - Verify users can sort items by price (Low → High, High → Low)")
    public void verifySortItemsByPrice() {
        log.info("=== START TC_04: Verify users can sort items by price ===");
        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();
        ShopPage shopPage = homePage.navigateToShopPage();
        shopPage.switchView("list");

        shopPage.sortBy(Constants.PRICE_LOW_TO_HIGH);
        List<Double> pricesLowToHigh = shopPage.getAllProductPrices();
        List<Double> expectedLowToHigh = new ArrayList<>(pricesLowToHigh);
        Collections.sort(expectedLowToHigh);
        softAssert.assertEquals(pricesLowToHigh, expectedLowToHigh,
                "❌ Products are not sorted correctly (Low → High)");

        shopPage.sortBy(Constants.PRICE_HIGH_TO_LOW);
        List<Double> pricesHighToLow = shopPage.getAllProductPrices();
        List<Double> expectedHighToLow = new ArrayList<>(pricesHighToLow);
        expectedHighToLow.sort(Collections.reverseOrder());
        softAssert.assertEquals(pricesHighToLow, expectedHighToLow,
                "❌ Products are not sorted correctly (High → Low)");

        softAssert.assertAll();
        log.info("=== END TC_04: Verify users can sort items by price ===");
    }
}
