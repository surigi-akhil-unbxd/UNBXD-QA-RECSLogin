package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.ExpActions;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import com.google.gson.JsonObject;

public class BoutiqueExperienceTest extends BaseTest {

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
    @FileToTest("recsTestData/BoutiqueExperienceTestData.json")
    public void createBoutiqueExperience(JsonObject dataMap) throws Exception {
        expActions.navigateToExperiencePage();
        expActions.handleAllPopups();
        expActions.clickCreateExperienceBtn();
        String experienceName = expActions.enterRandomExperienceName();
        expActions.selectPageByName(dataMap.get("pageName").getAsString());
        expActions.selectWidgetByName(dataMap.get("widgetName").getAsString());
        expActions.selectTemplateByName(dataMap.get("desktopTemplate").getAsString());
        expActions.selectTemplateByName(dataMap.get("mobileTemplate").getAsString());
        expActions.selectCustomAlgo(dataMap.get("customAlgo").getAsString());
        expActions.clickSaveButton();

        // Wait for the experience to appear in the list (robust wait, no sleep)
        expActions.waitForAndClickExperienceInList(experienceName);

        // Optionally, edit experience if EditWidgetType is present
        String editWidgetType = dataMap.get("EditWidgetType").getAsString();
        if (editWidgetType != null && !editWidgetType.trim().isEmpty()) {
            expActions.handleAllPopups();
            expActions.clickEditExpButton();
            expActions.selectWidgetByName(editWidgetType);
            expActions.clickSaveButton();
            // Wait for the experience to reappear in the list after edit
            expActions.waitForAndClickExperienceInList(experienceName);
        }

        // Delete experience (robust, no sleep)
        expActions.handleAllPopups();
        expActions.clickDeleteExperienceButton();
        expActions.clickConfirmDeleteButton();
        // Optionally, wait for the experience to disappear from the list
        // (could add expActions.isExperienceNotPresentInList(experienceName) if implemented)
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