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

public class MHstandardWidgettemplateTest extends BaseTest {

    private TemplatesActions1 templatesActions;
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
            templatesActions = new TemplatesActions1(driver);
            expActions = new ExpActions(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "getTestDataFromFile", dataProviderClass = ResourceLoader.class)
    @FileToTest("recsTestData/MHstandardWidgettemplateTestData.json")
    public void testTemplatesPageElementsInteraction(JsonObject dataMap) {
        expActions.handleAllPopups();
        templatesActions.navigateToTemplatesPage();
        templatesActions.openTemplateCreation();
        templatesActions.fillTemplateForm(dataMap);
        
        
        templatesActions.applyTemplateButton();
        templatesActions.isTemplateUpdatedSuccessfully();
        templatesActions.searchTemplateName(dataMap.get("TemplateName").getAsString());
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
        templatesActions.searchTemplateByName(dataMap.get("TemplateName").getAsString());
        templatesActions.hoverOnTemplateName(dataMap.get("TemplateName").getAsString());
        templatesActions.clickDeleteTemplateBtn();
        templatesActions.clickConfirmDeleteBtn();
        templatesActions.isTemplateDeletedSuccessfully();
        templatesActions.clearSearchInput();
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