package UnbxdTests.testNG.consoleui;

import core.consoleui.actions.ExpActions;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.*;
import java.util.*;
import com.google.gson.JsonObject;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;

public class JourneyExperienceTest extends UnbxdTests.testNG.ui.BaseTest {

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
    @FileToTest("recsTestData/journeyExperienceTestdata.json")
    public void createJourneyExperience(JsonObject dataMap) throws Exception {
        expActions.navigateToExperiencePage();
        expActions.handleAllPopups();
        expActions.clickCreateExperienceBtn();
        String experienceName = expActions.enterRandomExperienceName();

        String pageName = dataMap.get("pageName").getAsString();
        String widgetName = dataMap.get("widgetName").getAsString();
        String customAlgo = dataMap.get("customAlgo").getAsString();
        String editWidgetType = dataMap.get("EditWidgetType").getAsString();

        expActions.selectPageByName(pageName);
        expActions.selectWidgetByName(widgetName);
        expActions.selectCustomAlgo(customAlgo);
        expActions.clickSaveButton();
        Thread.sleep(3000);
        expActions.searchExperienceByName(experienceName);
       
        Thread.sleep(5000);

        // Edit experience
        expActions.waitForAndClickExperienceInList(experienceName);
        Thread.sleep(5000);
        expActions.clickEditExpButton();
        if (editWidgetType != null && !editWidgetType.trim().isEmpty()) {
            expActions.selectWidgetByName(editWidgetType);
        }
        expActions.clickSaveButton();
        Thread.sleep(5000);

        // Delete experience
        expActions.waitForAndClickExperienceInList(experienceName);
        Thread.sleep(5000);
        expActions.clickDeleteExperienceButton();
        expActions.clickConfirmDeleteButton();
        expActions.searchExperienceByName(experienceName);
        expActions.isExperienceNotPresentInList(experienceName);
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