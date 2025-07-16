package UnbxdTests.testNG.login;

import UnbxdTests.testNG.ui.BaseTest;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import lib.Helper;

public class LoginTest extends BaseTest {
    @Page
    LoginActions loginActions;
    private static final String COOKIES_PATH = "cookies.json";

    @BeforeClass
    public void setUp() {
        try {
            super.setUp();
            this.initFluent(driver);
            initTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginAndSaveCookies() {
        // Perform login (siteId=1, userId=1 or as needed)
        loginActions.login(1, 1);
        // Save cookies after successful login
        Helper.saveCookiesToFile(driver, COOKIES_PATH);
        System.out.println("[LoginTest] Cookies saved after login.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
        lib.Helper.tearDown();
    }
} 