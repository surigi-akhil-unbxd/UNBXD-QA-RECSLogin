package UnbxdTests.testNG.consoleui;

import UnbxdTests.testNG.ui.BaseTest;
import core.consoleui.actions.TemplatesActions1;
import core.ui.actions.LoginActions;
import org.fluentlenium.core.annotation.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import core.consoleui.actions.ExpActions;
import UnbxdTests.testNG.dataProvider.ResourceLoader;
import lib.annotation.FileToTest;
import com.google.gson.JsonObject;

public class UploadDHstandardTemplateTest extends BaseTest {

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
            templatesActions = new TemplatesActions1(driver);
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
    @FileToTest("recsTestData/DHstandardWidgettemplateTestData.json")
    public void uploadTemplateNavigation(JsonObject dataMap) {
        expActions.handleAllPopups();
        templatesActions.navigateToTemplatesPage();
        templatesActions.clickCreateTemplateBtn();
        templatesActions.clickUploadTemplateBtn();
        templatesActions.fillTemplateForm(dataMap);
        templatesActions.clickNextButton();
        templatesActions.enterRandomTemplateName();
        templatesActions.clickUploadFromDevice();
        templatesActions.uploadTemplateFile("DHstandardWidgettemplate.json");
        templatesActions.clickProceedBtn();

        // Replicate DHstandardWidgettemplateTest flow after creation
        templatesActions.isHorizontalWidgetDisplayed();
        templatesActions.applyTemplateButton();
        templatesActions.isTemplateUpdatedSuccessfully();
        templatesActions.searchTemplateName(dataMap.get("TemplateName").getAsString());
        templatesActions.clickEditTemplateBtn();
        templatesActions.clickYesBtn();
        try { Thread.sleep(10000); } catch (InterruptedException e) { e.printStackTrace(); }
        templatesActions.clickApplyTemplateBtn();
        templatesActions.clickProceedTemplateBtn();
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        templatesActions.searchTemplateByName(dataMap.get("TemplateName").getAsString());
        templatesActions.hoverOnTemplateName(dataMap.get("TemplateName").getAsString());
        templatesActions.clickDeleteTemplateBtn();
        templatesActions.clickConfirmDeleteBtn();
        templatesActions.isTemplateDeletedSuccessfully();
        templatesActions.clearSearchInput();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Test(dataProvider = "getTestDataFromFile", dataProviderClass = ResourceLoader.class)
    @FileToTest("recsTestData/DVstandardWidgetTestdata.json")
    public void uploadTemplateNavigationDV(JsonObject dataMap) {
        expActions.handleAllPopups();
        templatesActions.navigateToTemplatesPage();
        templatesActions.clickCreateTemplateBtn();
        templatesActions.clickUploadTemplateBtn();
        templatesActions.fillTemplateForm(dataMap);
        templatesActions.clickNextButton();
        templatesActions.enterRandomTemplateName();
        templatesActions.clickUploadFromDevice();
        templatesActions.uploadTemplateFile("DVstandardWidgettemplate.json");
        templatesActions.clickProceedBtn();

        // Replicate DVstandardWidgettemplateTest flow after creation
        templatesActions.isHorizontalWidgetDisplayed();
        templatesActions.applyTemplateButton();
        templatesActions.isTemplateUpdatedSuccessfully();
        templatesActions.searchTemplateName(dataMap.get("TemplateName").getAsString());
        templatesActions.clickEditTemplateBtn();
        templatesActions.clickYesBtn();
        try { Thread.sleep(10000); } catch (InterruptedException e) { e.printStackTrace(); }
        templatesActions.clickApplyTemplateBtn();
        templatesActions.clickProceedTemplateBtn();
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        templatesActions.searchTemplateByName(dataMap.get("TemplateName").getAsString());
        templatesActions.hoverOnTemplateName(dataMap.get("TemplateName").getAsString());
        templatesActions.clickDeleteTemplateBtn();
        templatesActions.clickConfirmDeleteBtn();
        templatesActions.isTemplateDeletedSuccessfully();
        templatesActions.clearSearchInput();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }
} 