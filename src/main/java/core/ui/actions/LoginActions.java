package core.ui.actions;

import core.ui.page.LoginPage;
import core.ui.page.WelcomePage;
import lib.EnvironmentConfig;
import lib.UrlMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.fluentlenium.core.Fluent;
import java.util.concurrent.TimeUnit;
import org.fluentlenium.core.annotation.Page;

public class LoginActions extends WelcomePage {
    private WebDriverWait wait;
    @Page
    private LoginPage page;

    @FindBy(css=".unx-login-title")
    public FluentWebElement loginTitle;

    @FindBy(css = ".unx-login-btn")
    public FluentWebElement signIn;

    @FindBy(name = "email")
    public FluentWebElement emailInputBox;

    @FindBy(name = "password")
    public FluentWebElement passwordInputBox;

    public LoginActions() {
        super();
    }

    public LoginActions(WebDriver driver) {
        super(driver);
    }

    private WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(), 30);
        }
        return wait;
    }

    public void login() {
        // Default login Uses SiteId 0 and UserId 1
        login(0,1);
    }

    public void login(int siteId, int userId) {
        EnvironmentConfig.setContext(userId, siteId);
        String email = EnvironmentConfig.getEmail();
        String password = EnvironmentConfig.getPassword();
        loginWith(email, password);
    }

    public String getUrl() {
        return UrlMapper.LOGIN.getUrlPathFromAppUrl(EnvironmentConfig.getLoginUrl());
    }

    public void loginWith(String email, String password) {
        try {
            goTo(page.getUrl());
        awaitForPageToLoad();
            awaitForElementPresence(page.emailInputBox);
            page.emailInputBox.fill().with(email);
            awaitForElementPresence(page.passwordInputBox);
            page.passwordInputBox.fill().with(password);
            awaitForElementPresence(page.signIn);
            click(page.signIn);
        awaitForPageToLoad();
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }

    public void login(String email, String password) {
        loginWith(email, password);
    }
}
