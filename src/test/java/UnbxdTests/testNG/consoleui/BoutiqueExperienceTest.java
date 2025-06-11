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
            this.initFluent(driver);
            initTest();
            loginActions.login(1, 2);
            expActions = new ExpActions(driver);
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
        Thread.sleep(5000);

        // Optionally, edit experience if EditWidgetType is present
        String editWidgetType = dataMap.get("EditWidgetType").getAsString();
        if (editWidgetType != null && !editWidgetType.trim().isEmpty()) {
            expActions.waitForAndClickExperienceInList(experienceName);
            Thread.sleep(5000);
            expActions.clickEditExpButton();
            expActions.selectWidgetByName(editWidgetType);
            expActions.clickSaveButton();
            Thread.sleep(5000);
        }

        // Delete experience
        expActions.waitForAndClickExperienceInList(experienceName);
        Thread.sleep(5000);
        expActions.clickDeleteExperienceButton();
        expActions.clickConfirmDeleteButton();
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