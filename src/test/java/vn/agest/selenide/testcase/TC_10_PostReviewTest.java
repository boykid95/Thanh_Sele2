package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import vn.agest.selenide.pageObjects.BasePage;
import vn.agest.selenide.pageObjects.ProductDetailPage;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

public class TC_10_PostReviewTest extends BaseTest {

    @Test(description = "TC_10: Verify users can post a review")
    @Description("Verifies a logged-in user can post a review with a random star rating and see it appear correctly.")
    public void verifyUserCanPostReview() {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();
        ShopPage shopPage = homePage.navigateToShopPage();
        ProductDetailPage productDetailPage = shopPage.clickRandomProduct();

        int randomStars = BasePage.getRandomNumber(1, 5);
        String reviewComment = "A " + randomStars + "-star test review written at " + System.currentTimeMillis();
        productDetailPage.goToReviewsTab();
        productDetailPage.submitReview(randomStars, reviewComment);

        productDetailPage.goToReviewsTab();
        String actualComment = productDetailPage.getLatestCommentText();
        int actualRating = productDetailPage.getLatestCommentRating();

        softAssert.assertEquals(actualComment, reviewComment, "❌ Comment content does not match.");
        softAssert.assertEquals(actualRating, randomStars, "❌ The number of rating stars does not match.");

        softAssert.assertAll();
    }
}