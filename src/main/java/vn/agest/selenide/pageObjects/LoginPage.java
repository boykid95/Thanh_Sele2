package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.User;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage extends BasePage {

    private final SelenideElement usernameInput = $x("//input[@id='username']");
    private final SelenideElement passwordInput = $x("//input[@id='password']");
    private final SelenideElement loginButton = $x("//button[@name='login']");

    public LoginPage() {
        super(new ElementHelper(),PageType.LOGIN_PAGE);
    }

    @Step("Login with valid credentials")
    public void loginWithValidCredentials() {
        enterUsername(User.defaultUser().getUsername());
        enterPassword(User.defaultUser().getPassword());
        clickLoginButton();
    }

    @Step("Enter username: {username}")
    private void enterUsername(String username) {
        elementHelper.waitForElementVisible(usernameInput, "Username Input");
        usernameInput.setValue(username);
    }

    @Step("Enter password: {password}")
    private void enterPassword(String password) {
        elementHelper.waitForElementVisible(passwordInput, "Password Input");
        passwordInput.setValue(password);
    }

    @Step("Click on Login button")
    private void clickLoginButton() {
        elementHelper.clickToElement(loginButton, "Login Button");
    }
}
