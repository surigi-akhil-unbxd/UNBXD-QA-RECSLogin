package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.TemplatesActions1;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import com.google.gson.JsonObject;
import core.consoleui.actions.ExpActions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class MHstandardWidgettemplateTest extends BaseTest {

    private TemplatesActions1 templatesActions;
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
            templatesActions = new core.consoleui.actions.TemplatesActions1(driver);
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
    @FileToTest("recsTestData/MHstandardWidgettemplateTestData.json")
    public void testTemplatesPageElementsInteraction(JsonObject dataMap) {
        // Popups are now handled automatically during navigation
        templatesActions.navigateToTemplatesPage();
        templatesActions.openTemplateCreation();
        templatesActions.fillTemplateForm(dataMap);
        
        
        templatesActions.applyTemplateButton();
        templatesActions.isTemplateUpdatedSuccessfully();
        
        // Wait for 1 second after template update
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Get the template name for verification
        String templateName = dataMap.get("TemplateName").getAsString();
        templatesActions.searchTemplateName(templateName);
        
        templatesActions.clickEditTemplateBtn();
        // Wait for 1 second after clicking Edit Template
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        templatesActions.clickYesBtn();
        // OPTIMIZATION: Reduced wait time from 10000ms to 1000ms
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        templatesActions.clickApplyTemplateBtn();
        templatesActions.clickProceedTemplateBtn();
        // OPTIMIZATION: Reduced wait time from 5000ms to 1000ms
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        templatesActions.searchTemplateByName(templateName);
        templatesActions.hoverOnTemplateName(templateName);
        templatesActions.clickDeleteTemplateBtn();
        templatesActions.clickConfirmDeleteBtn();
        templatesActions.isTemplateDeletedSuccessfully();
        
        // Verify template deletion completely
        templatesActions.verifyTemplateDeletionComplete(templateName);
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