package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.ExpActions;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import com.google.gson.JsonObject;

public class ExpTest extends BaseTest {
    
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
    @FileToTest("recsTestData/ExperienceTestData.json")
    public void createExperienceTest(JsonObject dataMap) throws Exception {
        expActions.navigateToExperiencePage();
        expActions.handleAllPopups();
        expActions.clickCreateExperienceBtn();
        // Wait for the field to be non-empty (UI has set default)
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 5)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.not(
                org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe(
                    expActions.getExperienceNameInput(), "value", ""
                )
            ));
        String experienceName = expActions.enterRandomExperienceName();

        String pageName = dataMap.get("pageName").getAsString();
        String widgetName = dataMap.get("widgetName").getAsString();
        String desktopTemplate = dataMap.get("desktopTemplate").getAsString();
        String mobileTemplate = dataMap.get("mobileTemplate").getAsString();
        String customAlgo = dataMap.get("customAlgo").getAsString();
        String editWidgetType = dataMap.get("EditWidgetType").getAsString();

        // Create experience
        expActions.selectPageByName(pageName);
        expActions.selectWidgetByName(widgetName);
        expActions.selectTemplateByName(desktopTemplate);
        expActions.selectTemplateByName(mobileTemplate);
        expActions.selectCustomAlgo(customAlgo);
        expActions.clickSaveButton();
        Thread.sleep(1000);
        expActions.searchExperienceByName(experienceName);

        
        Thread.sleep(1000);

        // Edit experience
        expActions.waitForAndClickExperienceInList(experienceName);
        Thread.sleep(1000);
        expActions.clickEditExpButton();
        if (editWidgetType != null && !editWidgetType.trim().isEmpty()) {
            expActions.selectWidgetByName(editWidgetType);
        }
        expActions.clickSaveButton();
        Thread.sleep(1000);

        // Delete experience
        expActions.waitForAndClickExperienceInList(experienceName);
        Thread.sleep(1000);
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