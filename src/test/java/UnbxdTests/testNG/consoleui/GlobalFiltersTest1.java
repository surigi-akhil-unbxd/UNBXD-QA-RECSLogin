package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.GlobalFiltersActions1;
import core.consoleui.pages.GlobalFiltersPage1;
import core.ui.actions.LoginActions;
import lib.Helper;
import org.fluentlenium.core.annotation.Page;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GlobalFiltersTest1 extends BaseTest {

    @Page
    GlobalFiltersActions1 globalFiltersActions;

    @Page
    GlobalFiltersPage1 globalFiltersPage;

    @Page
    LoginActions loginActions;

    @BeforeClass
    public void setUp() {
        try {
            super.setUp();
            lib.EnvironmentConfig.setContext(1, 1);
            this.initFluent(driver);
            initTest();
            boolean cookiesRestored = lib.Helper.restoreCookiesFromFile(driver, "cookies.json", lib.EnvironmentConfig.getLoginUrl());
            if (!cookiesRestored) {
                throw new IllegalStateException("Cookies not found. Please run LoginTest first.");
            }
            driver.navigate().refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Create a new global filter set")
    public void createGlobalFilterTest() throws InterruptedException {
        goTo(globalFiltersPage);
        globalFiltersActions.awaitForPageToLoad();
        globalFiltersActions.navigateToGlobalFilters();
        globalFiltersActions.createNewFilterSet();
        Assert.assertTrue(globalFiltersActions.isSuccessMessageDisplayed(), "Failed to create global filter set");
    }

    @AfterClass
    public void tearDown() {
        driver.close();
        driver.quit();
        Helper.tearDown();
    }
} 