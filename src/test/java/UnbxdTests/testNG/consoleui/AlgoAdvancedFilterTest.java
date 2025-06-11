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
            this.initFluent(driver);
            initTest();
            loginActions.login(1, 1);
            expActions = new ExpActions(driver);
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