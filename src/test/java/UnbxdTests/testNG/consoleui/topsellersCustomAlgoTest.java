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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

public class topsellersCustomAlgoTest extends BaseTest {

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
            customAlgorithmsActions = new core.consoleui.actions.CustomAlgorithmsActions(driver);
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
    @FileToTest("recsTestData/topsellersCustomAlgoTestData.json")
    public void testCustomAlgorithm(JsonObject dataMap) {
        // Popups are now handled automatically during navigation
        customAlgorithmsActions.navigateToCustomAlgorithmsPage();
        customAlgorithmsActions.clickMinimizeTitle();
        customAlgorithmsActions.clickCreateNewButton();
    
        String algoName = customAlgorithmsActions.enterRandomCursorAlgoName();
        customAlgorithmsActions.selectAlgorithmByName(dataMap.get("customAlgo").getAsString());
        int lookbackPeriod = dataMap.get("noOfSlots").getAsInt();
        customAlgorithmsActions.setLookBackPeriod(lookbackPeriod);
        customAlgorithmsActions.clickSaveButton();
        
        // Wait for the search input to be visible before searching
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Search for Custom Algorithm']")));
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customAlgorithmsActions.clickEditButton();
        customAlgorithmsActions.selectAlgorithmByName(dataMap.get("editCustomAlgo").getAsString());
        customAlgorithmsActions.clickSaveButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Print all algorithm names in the list for debugging before delete
        for (WebElement el : driver.findElements(By.cssSelector(".item-name"))) {
            System.out.println("[DEBUG] Found algo in list: '" + el.getText() + "'");
        }
        customAlgorithmsActions.hoverAndClickOnCreatedAlgorithm();
        customAlgorithmsActions.clickDeleteButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-footer button.btn-primary")));
        customAlgorithmsActions.clickProceedButton();
        customAlgorithmsActions.searchCustomAlgorithmByName(algoName);
        // Wait for the algorithm to be removed from the list
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class,'list-crud-items')]//*[contains(text(),'" + algoName + "')]")));
        // Add debug log for completion
        System.out.println("[DEBUG] testCustomAlgorithm completed for algoName: " + algoName);
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