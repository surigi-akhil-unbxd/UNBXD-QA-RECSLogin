package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.CoreAlgorithmsActions;
import core.consoleui.pages.CoreAlgorithmsPage;
import core.ui.actions.LoginActions;
import core.consoleui.actions.ExpActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import com.google.gson.JsonObject;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CoreAlgorithmsTest extends BaseTest {

    private CoreAlgorithmsActions coreAlgorithmsActions;
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
            coreAlgorithmsActions = new core.consoleui.actions.CoreAlgorithmsActions(driver);
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
    @FileToTest("recsTestData/coreAlgoTestData.json")
    public void testNavigateToCoreAlgorithmsPage(JsonObject dataMap) {
        expActions.handleAllPopups();
        coreAlgorithmsActions.navigateToCoreAlgorithmsPage();
        coreAlgorithmsActions.clickCoreAlgorithms();
        coreAlgorithmsActions.clickMinimizeTitle();
        coreAlgorithmsActions.clickCTLConfigureButton();
        // Wait for 2 seconds to ensure the UI is ready after clicking CTL Configure Button
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // --- Custom logic: Search and delete if present ---
        String preCheckProductId = "AAM15MB01";
        coreAlgorithmsActions.enterProductIdInSearchInput(preCheckProductId);
        try {
            Thread.sleep(1000); // Wait for search results to update
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (coreAlgorithmsActions.isProductRulePresentInListing(preCheckProductId)) {
            coreAlgorithmsActions.clickParentIdInListing(preCheckProductId);
            coreAlgorithmsActions.clickDeleteIcon();
            coreAlgorithmsActions.clickProceedButton();
            try {
                Thread.sleep(2000); // Wait for deletion to complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // --- End custom logic ---

        coreAlgorithmsActions.clickCreateButton();
        
        
        String productId = dataMap.get("parentID").getAsString(); // or whichever key you want from your test data
        coreAlgorithmsActions.enterProductId(productId);
        String childProductId = dataMap.get("childID").getAsString();
        coreAlgorithmsActions.enterChildProductId(childProductId);
        coreAlgorithmsActions.clickSaveButton();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coreAlgorithmsActions.enterProductIdInSearchInput(productId);
        coreAlgorithmsActions.clickParentIdInListing(productId);
        coreAlgorithmsActions.clickEditIcon();
        // Wait for 3 seconds to ensure the UI is ready after clicking Edit Icon
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coreAlgorithmsActions.clickAddButton();
        
         String childProductId1 = dataMap.get("childID1").getAsString();
        
         coreAlgorithmsActions.enterChildProductId1(childProductId1);
        coreAlgorithmsActions.clickSaveButton();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coreAlgorithmsActions.enterProductIdInSearchInput(productId);
        coreAlgorithmsActions.clickParentIdInListing(productId);
        coreAlgorithmsActions.clickDeleteIcon();
        coreAlgorithmsActions.clickProceedButton();
    }
} 