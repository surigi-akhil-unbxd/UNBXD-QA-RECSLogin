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

public class UploadMHstandardTemplateTest extends BaseTest {

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
    @FileToTest("recsTestData/MHstandardWidgetTestData.json")
    public void uploadTemplateNavigation(JsonObject dataMap) {
        expActions.handleAllPopups();
        templatesActions.navigateToTemplatesPage();
        templatesActions.clickCreateTemplateBtn();
        templatesActions.clickUploadTemplateBtn();
        templatesActions.fillTemplateForm(dataMap);
        templatesActions.clickNextButton();
        templatesActions.enterRandomTemplateName();
        templatesActions.clickUploadFromDevice();
        templatesActions.uploadTemplateFile("MHstandardWidgettemplate.json");
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
} 