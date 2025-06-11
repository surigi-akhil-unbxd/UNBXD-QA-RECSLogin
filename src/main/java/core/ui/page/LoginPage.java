package core.ui.page;

import lib.UrlMapper;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends UiBase {

    @FindBy(css=".unx-login-title")
    public FluentWebElement loginTitle;

    @FindBy(css = ".unx-login-btn")
    public FluentWebElement signIn;

    @FindBy(name = "email")
    public FluentWebElement emailInputBox;

    @FindBy(name = "password")
    public FluentWebElement passwordInputBox;

    public LoginPage() {
        super();
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public String getUrl()
    {
        return UrlMapper.LOGIN.getUrlPath();
    }
}
