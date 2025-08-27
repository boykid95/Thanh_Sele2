package vn.agest.selenide.pageObjects;

import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;

public class HomePage extends BasePage {

    public HomePage() {
        super(new ElementHelper(),PageType.HOME_PAGE);
    }

}
