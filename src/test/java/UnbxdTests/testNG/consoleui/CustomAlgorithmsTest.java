package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.CustomAlgorithmsActions;
import core.consoleui.pages.CustomAlgorithmsPage;
import core.ui.actions.LoginActions;
import core.consoleui.actions.ExpActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import com.google.gson.JsonObject;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import org.testng.Assert;

public class CustomAlgorithmsTest extends BaseTest {

    private CustomAlgorithmsActions customAlgorithmsActions;
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
            customAlgorithmsActions = new CustomAlgorithmsActions(driver);
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
    @FileToTest("recsTestData/CustomAlgorithmsTestData.json")
    public void testCustomAlgorithm(JsonObject dataMap) {
        expActions.handleAllPopups();
        customAlgorithmsActions.navigateToCustomAlgorithmsPage();
        customAlgorithmsActions.clickMinimizeTitle();
        customAlgorithmsActions.clickCreateNewButton();
    
        String algoName = customAlgorithmsActions.enterRandomCursorAlgoName();
        customAlgorithmsActions.selectAlgorithmByName(dataMap.get("customAlgo").getAsString());
        customAlgorithmsActions.clickSaveButton();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
       // customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        customAlgorithmsActions.clickEditButton();
        customAlgorithmsActions.selectAlgorithmByName(dataMap.get("editCustomAlgo").getAsString());
        customAlgorithmsActions.clickSaveButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        try {
            Thread.sleep(1000); // Wait for 3 seconds before clicking on created algorithm for delete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        customAlgorithmsActions.clickDeleteButton();
        customAlgorithmsActions.clickProceedButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        boolean isNotPresent = !customAlgorithmsActions.isAlgorithmPresentInListing(algoName);
        Assert.assertTrue(isNotPresent, "Custom algorithm '" + algoName + "' should not be present in the list");
        



        
        
        
        
     
        
       
        
        // customAlgorithmsActions.selectNoOfSlots(dataMap.get("noOfSlots").getAsString());

        // Add further steps as needed, e.g., save, verify, edit, delete, etc.
        // Add waits or sleeps if UI needs time to update
    }

    // Add more test methods as needed

    // @AfterClass
    // public void tearDown() {
    //     if (driver != null) {
    //         driver.close();
    //         driver.quit();
    //     }
    //     lib.Helper.tearDown();
    // }
} 