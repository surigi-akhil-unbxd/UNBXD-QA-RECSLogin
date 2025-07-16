// GBFiltersTest.java temporarily commented out to resolve compilation errors.

package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.GBFiltersActions;
import core.consoleui.pages.GBFiltersPage;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;

public class GBFiltersTest extends BaseTest {

    @Page
    GBFiltersActions gbFiltersActions;

    @Page
    GBFiltersPage gbFiltersPage;

    @Page
    LoginActions loginActions;

    private core.consoleui.actions.ExpActions expActions;

    @BeforeClass
    public void setUp() {
        try {
            super.setUp();
            lib.EnvironmentConfig.setContext(1, 1);
            this.initFluent(driver);
            initTest();
            expActions = new core.consoleui.actions.ExpActions(driver);
            boolean cookiesRestored = lib.Helper.restoreCookiesFromFile(driver, "cookies.json", lib.EnvironmentConfig.getLoginUrl());
            if (!cookiesRestored) {
                throw new IllegalStateException("Cookies not found. Please run LoginTest first.");
            }
            driver.navigate().refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "getTestDataFromFile", dataProviderClass = ResourceLoader.class)
    @FileToTest("recsTestData/GBfiltersTestData.json")
    public void testGBFiltersWorkflow(JsonObject dataMap) throws InterruptedException {
        // 1. Navigate to GBFilters page
        goTo(gbFiltersPage);

        // 2. Minimize this
        gbFiltersActions.clickMinimizeThisBtn();

        // 3. Add new filter set
        gbFiltersActions.clickAddNewFilterSetBtn();

        // 4. Add filter set (attribute, operator, action from test data)
        gbFiltersActions.selectAttribute(dataMap.get("attribute").getAsString());
        gbFiltersActions.selectOperator(dataMap.get("operator").getAsString());
        gbFiltersActions.selectAction(dataMap.get("value").getAsString());

        // 5. Save
        gbFiltersActions.clickSaveBtn();

        // 6. Edit the filter set
        gbFiltersActions.clickEditIcon();
        gbFiltersActions.selectAction(dataMap.get("editValue").getAsString());
        gbFiltersActions.clickSaveBtn();

        // 7. Delete the filter set
        gbFiltersActions.clickDeleteIcon();
        gbFiltersActions.clickYesBtn();
    }

    @Test
    public void sanityTest() {
        System.out.println("Sanity test ran!");
    }

    @AfterClass
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
