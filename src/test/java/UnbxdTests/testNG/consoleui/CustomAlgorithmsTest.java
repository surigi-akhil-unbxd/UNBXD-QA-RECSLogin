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
            this.initFluent(driver);
            initTest();
            loginActions.login(1, 1);
            customAlgorithmsActions = new CustomAlgorithmsActions(driver);
            expActions = new ExpActions(driver);
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
        customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        customAlgorithmsActions.clickEditButton();
        customAlgorithmsActions.selectAlgorithmByName(dataMap.get("editCustomAlgo").getAsString());
        customAlgorithmsActions.clickSaveButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        try {
            Thread.sleep(3000); // Wait for 3 seconds before clicking on created algorithm for delete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        customAlgorithmsActions.clickDeleteButton();
        customAlgorithmsActions.clickProceedButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        boolean isNotPresent = !customAlgorithmsActions.isAlgorithmPresentInListing(algoName);
        System.out.println("Custom algorithm '" + algoName + "' does not exist in list: " + isNotPresent);
        
        customAlgorithmsActions.clearSearchCustomAlgorithmInput();
        
        
        
        
     
        
       
        
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