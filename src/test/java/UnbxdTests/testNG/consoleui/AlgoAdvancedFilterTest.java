package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.ExpActions;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import com.google.gson.JsonObject;

public class AlgoAdvancedFilterTest extends BaseTest {

    private ExpActions expActions;

    @Page
    LoginActions loginActions;

    @BeforeClass
    public void setUp() {
        try {
            super.setUp();
            lib.EnvironmentConfig.setContext(1, 1);
            this.initFluent(driver);
            initTest();
            expActions = new ExpActions(driver);
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
    @FileToTest("recsTestData/AdvancedFilterTestData.json")
    public void testAdvancedFilterDropdown(JsonObject dataMap) {
        expActions.handleAllPopups();
        expActions.navigateToExperiencePage();
        expActions.clickAdvancedFilterBtn();
        expActions.selectAdvancedFilterDropdownOption(dataMap.get("filterOption").getAsString());
        if (dataMap.has("selectOption")) {
            expActions.selectDropdownOption(dataMap.get("selectOption").getAsString());
            expActions.clickAddIconBtn();
            expActions.clickResetAndRemoveBtn();
            expActions.clickAdvancedFilterLink();
        }
    }
} 