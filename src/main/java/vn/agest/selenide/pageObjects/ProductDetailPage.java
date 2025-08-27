package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Log4j
public class ProductDetailPage extends BasePage {

    private final String reviewsTabLocator = "#tab_reviews";
    private final String reviewFormLocator = "#review_form";
    private final String starRatingLocator = "a.star-%d";
    private final String commentInputLocator = "p.comment-form-comment #comment";
    private final String submitButtonLocator = "#submit";
    private final String allCommentsLocator = "ol.commentlist li";
    private final String latestCommentStarsLocator = "div.star-rating span";
    private final String latestCommentTextLocator = "div.description p";

    public ProductDetailPage() {
        super(new ElementHelper(), null);
    }

    @Step("Go to the Reviews tab")
    public void goToReviewsTab() {
        SelenideElement reviewsTab = $(reviewsTabLocator);
        reviewsTab.scrollIntoView(true);
        elementHelper.jsClickToElement(reviewsTab, "Reviews Tab");
        $(reviewFormLocator).shouldBe(visible);
    }

    @Step("Submit a review with {stars} stars and comment: '{comment}'")
    public void submitReview(int stars, String comment) {
        ElementsCollection allComments = $$(allCommentsLocator);
        int initialCommentCount = allComments.size();

        SelenideElement starToClick = $(String.format(starRatingLocator, stars));
        elementHelper.clickToElement(starToClick, stars + "-star rating");
        $(commentInputLocator).setValue(comment);
        elementHelper.clickToElement($(submitButtonLocator), "Submit Review Button");

        allComments.shouldHave(sizeGreaterThan(initialCommentCount));
    }

    @Step("Get the text of the latest comment")
    public String getLatestCommentText() {
        SelenideElement latestComment = $$(allCommentsLocator).last().shouldBe(visible);
        return latestComment.$(latestCommentTextLocator).getText();
    }

    @Step("Get the star rating of the latest comment")
    public int getLatestCommentRating() {
        SelenideElement latestComment = $$(allCommentsLocator).last().shouldBe(visible);
        SelenideElement latestCommentStars = latestComment.$(latestCommentStarsLocator);
        String styleAttribute = latestCommentStars.getAttribute("style");
        String widthValue = styleAttribute.replaceAll("[^0-9]", "");
        int widthPercentage = Integer.parseInt(widthValue);

        return widthPercentage / 20;
    }
}
