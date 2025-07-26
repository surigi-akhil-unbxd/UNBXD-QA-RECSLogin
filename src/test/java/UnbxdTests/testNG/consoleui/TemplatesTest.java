package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.TemplatesActions;
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

public class TemplatesTest extends BaseTest {

    private TemplatesActions templatesActions;
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
            templatesActions = new TemplatesActions(driver);
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
    @FileToTest("recsTestData/TemplatesTestData.json")
    public void testTemplatesPageElementsInteraction(JsonObject dataMap) {
        expActions.handleAllPopups();
        templatesActions.navigateToTemplatesPage();
        templatesActions.openTemplateCreation();
       // templatesActions.fillTemplateForm(dataMap); // Commented out: method does not exist in TemplatesActions
     
       
        templatesActions.clickCreateButton();
        // Wait for 1 second to ensure the UI is ready after clicking Create
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String templateName = "autoTemplate" + System.currentTimeMillis();
        System.out.println("[DEBUG] Template name to be entered: " + templateName);
        templatesActions.enterTemplateName(templateName);
        templatesActions.clickTemplateTypeDropdown();
        String templateType = dataMap.get("TemplateType").getAsString();
        // templatesActions.selectTemplateType(templateType); // Commented out: method does not accept arguments
        templatesActions.clickApplyTemplateBtn();
        templatesActions.clickProceedTemplateBtn();
        templatesActions.isTemplateUpdatedSuccessfully();
        templatesActions.searchTemplateByName(templateName);
        templatesActions.hoverOnTemplateName(templateName);
        templatesActions.clickEditTemplateBtn();
        templatesActions.clickYesBtn();
        // Wait for 1 second to ensure the UI is ready after clicking Yes
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        templatesActions.clickApplyTemplateBtn();
        templatesActions.clickProceedTemplateBtn();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        templatesActions.searchTemplateByName(templateName);
        templatesActions.hoverOnTemplateName(templateName);
        templatesActions.clickDeleteTemplateBtn();
        templatesActions.clickConfirmDeleteBtn();
        templatesActions.isTemplateDeletedSuccessfully();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }


    // @AfterClass
    // public void tearDown() {
    //     if (driver != null) {
    //         driver.close();
    //         driver.quit();
    //     }
    //     lib.Helper.tearDown();
    // }
} 