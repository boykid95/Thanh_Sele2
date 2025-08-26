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

    private final SelenideElement reviewsTab = $("#tab_reviews");
    private final SelenideElement reviewForm = $("#review_form");
    private final String starRatingLocator = "a.star-%d";
    private final SelenideElement commentInput = $("p.comment-form-comment #comment");
    private final SelenideElement submitButton = $("#submit");

    private final ElementsCollection allComments = $$("ol.commentlist li");
    private final SelenideElement latestComment = allComments.last();
    private final SelenideElement latestCommentStars = latestComment.$("div.star-rating span");

    public ProductDetailPage() {
        super(new ElementHelper(), null);
    }

    @Step("Go to the Reviews tab")
    public void goToReviewsTab() {
        elementHelper.clickToElement(reviewsTab, "Reviews Tab");
        reviewForm.shouldBe(visible);
    }

    @Step("Submit a review with {stars} stars and comment: '{comment}'")
    public void submitReview(int stars, String comment) {
        int initialCommentCount = allComments.size();

        SelenideElement starToClick = $(String.format(starRatingLocator, stars));
        elementHelper.clickToElement(starToClick, stars + "-star rating");
        commentInput.setValue(comment);
        elementHelper.clickToElement(submitButton, "Submit Review Button");

        allComments.shouldHave(sizeGreaterThan(initialCommentCount));
    }

    @Step("Get the text of the latest comment")
    public String getLatestCommentText() {
        latestComment.shouldBe(visible);
        return latestComment.$("div.description p").getText();
    }

    @Step("Get the star rating of the latest comment")
    public int getLatestCommentRating() {
        latestComment.shouldBe(visible);
        String styleAttribute = latestCommentStars.getAttribute("style");
        String widthValue = styleAttribute.replaceAll("[^0-9]", "");
        int widthPercentage = Integer.parseInt(widthValue);
        int starRating = widthPercentage / 20;
        log.info("Extracted star rating. Style='".concat(styleAttribute)
                .concat("', Calculated stars=").concat(String.valueOf(starRating)));
        return starRating;
    }
}
