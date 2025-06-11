package core.consoleui.actions;

import core.consoleui.pages.TemplatesPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import lib.EnvironmentConfig;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class TemplatesActions {
    private WebDriver driver;
    private TemplatesPage page;

    public TemplatesActions(WebDriver driver) {
        this.driver = driver;
        this.page = PageFactory.initElements(driver, TemplatesPage.class);
    }

    // All template action methods removed for now

    public void clickTemplateSideMenuBtn() {
        page.templateSideMenuBtn.click();
    }

    public void clickMinimizeThisBtn() {
        page.minimizeThisBtn.click();
    }

    public void enterSearchInput(String text) {
        page.searchInput.clear();
        page.searchInput.sendKeys(text);
    }

    public void clickCreateTemplateBtn() {
        page.createTemplateBtn.click();
    }

    public void clickFiltersBtn() {
        page.filtersBtn.click();
    }

    public void clickCreateNewLink() {
        page.createNewLink.click();
    }

    public void clickUploadTemplateBtn() {
        page.uploadTemplateBtn.click();
    }

    public void selectDesktopRadioBtn() {
        if (!page.desktopRadioBtn.isSelected()) page.desktopRadioBtn.click();
    }

    public void selectMobileRadioBtn() {
        if (!page.mobileRadioBtn.isSelected()) page.mobileRadioBtn.click();
    }

    public void selectHorizontalRadioBtn() {
        if (!page.horizontalRadioBtn.isSelected()) page.horizontalRadioBtn.click();
    }

    public void selectVerticalRadioBtn() {
        if (!page.verticalRadioBtn.isSelected()) page.verticalRadioBtn.click();
    }

    public void clickCreateButton() {
        // Debug point: log before clicking create button
        System.out.println("[DEBUG] About to click the Create button in TemplatesActions.clickCreateButton()");
        // Hide beamerSelector overlay if present
        try {
            org.openqa.selenium.WebElement beamer = driver.findElement(org.openqa.selenium.By.id("beamerSelector"));
            if (beamer.isDisplayed()) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", beamer);
            }
        } catch (Exception ignored) {}
        page.createButton.click();
        // Wait for 3 seconds to ensure the UI is ready after clicking Create
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterTemplateName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(page.templateNameInput));
        wait.until(ExpectedConditions.elementToBeClickable(page.templateNameInput));
        page.templateNameInput.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", page.templateNameInput);
        page.templateNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        page.templateNameInput.sendKeys(name);
    }

    public void clickBackBtn() {
        page.backBtn.click();
    }

    public void clickAdvancedCodeEditorBtn() {
        page.advancedCodeEditorBtn.click();
    }

    public void clickSaveButton() {
        page.saveButton.click();
    }

    public void clickApplyTemplateBtn() {
        page.applyTemplateBtn.click();
    }

    public void clickTemplateTypeDropdown() {
        page.templateTypeDropdown.click();
    }

    public void enterWidgetSize(String size) {
        page.widgetSizeInput.clear();
        page.widgetSizeInput.sendKeys(size);
    }

    public void clickProductsInDisplayDropdown() {
        page.productsInDisplayDropdown.click();
    }

    public void checkGlobalAddonsCheckbox() {
        if (!page.globalAddonsCheckbox.isSelected()) page.globalAddonsCheckbox.click();
    }

    public void checkLikeButtonCheckbox() {
        if (!page.likeButtonCheckbox.isSelected()) page.likeButtonCheckbox.click();
    }

    public void checkDislikeButtonCheckbox() {
        if (!page.dislikeButtonCheckbox.isSelected()) page.dislikeButtonCheckbox.click();
    }

    public void checkAddToWishlistButtonCheckbox() {
        if (!page.addToWishlistButtonCheckbox.isSelected()) page.addToWishlistButtonCheckbox.click();
    }

    public void checkShareButtonCheckbox() {
        if (!page.shareButtonCheckbox.isSelected()) page.shareButtonCheckbox.click();
    }

    public void clickEditGlobalAddonsIcon() {
        page.editGlobalAddonsIcon.click();
    }

    public void clickUnitDropdown() {
        page.unitDropdown.click();
    }

    public void clickMaximumProductsDropdown() {
        page.maximumProductsDropdown.click();
    }

    public void selectRadioBtn(String value) {
        switch (value.toLowerCase()) {
            case "desktop":
                selectDesktopRadioBtn();
                break;
           
            case "horizontal":
                selectHorizontalRadioBtn();
                break;
            
        }
    }

    public void openTemplateCreation() {
        clickCreateTemplateBtn();
        clickCreateNewLink();
        // Wait for the modal/drawer to be visible (e.g., wait for the Medium selection label)
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.cssSelector("label.unbxd-card-select[for='desktop']")
            ));
    }

    public void selectMedium(String medium) {
        selectRadioBtn(medium);
    
    }

    public void selectLayout(String layout) {
        selectRadioBtn(layout);
    }

    public void enterTemplatedetails() {
        clickTemplateTypeDropdown();
        clickUnitDropdown();
        clickMaximumProductsDropdown();
        clickProductsInDisplayDropdown();
        checkGlobalAddonsCheckbox();
        checkLikeButtonCheckbox();
        checkDislikeButtonCheckbox();
        checkAddToWishlistButtonCheckbox();
        checkShareButtonCheckbox();
        clickApplyTemplateBtn();
    }

    public void selectTemplateType() {
        // Assumes the dropdown is already open
        java.util.List<org.openqa.selenium.WebElement> options = driver.findElements(
            org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span")
        );
        if (!options.isEmpty()) {
            org.openqa.selenium.WebElement option = options.get(0);
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(option))
                .click();
        } else {
            System.out.println("[WARN] No template type options found in the dropdown");
        }
    }

    public void navigateToTemplatesPage() {
        String siteId = EnvironmentConfig.getSiteId();
        String templatesUrl = EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/manage/templates";
        driver.get(templatesUrl);
    }

    public void selectMaximumProducts(String value) {
        // Assumes the dropdown is already open
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + value + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void selectProductsInDisplay(String value) {
        // Assumes the dropdown is already open
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + value + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void clickProceedTemplateBtn() {
        page.proceedTemplateBtn.click();
    }

    public void searchTemplateByName(String name) {
        page.searchInput.clear();
        page.searchInput.sendKeys(name);
    }

    public boolean isTemplateUpdatedSuccessfully() {
        // Checks for the 'Template updated successfully' toaster message
        org.openqa.selenium.By toasterLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'toast') and contains(.,'Template updated successfully')]");
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 5)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(toasterLocator));
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 5)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(toasterLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void hoverOnTemplateName(String name) {
        org.openqa.selenium.By templateNameLocator = org.openqa.selenium.By.xpath("//*[contains(text(), '" + name + "')]");
        org.openqa.selenium.WebElement element = new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(templateNameLocator));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public void clickEditTemplateBtn() {
        page.editTemplateBtn.click();
    }

    public void clickDeleteTemplateBtn() {
        page.deleteTemplateBtn.click();
    }

    public void clickConfirmDeleteBtn() {
        page.confirmDeleteBtn.click();
    }

    public void clickYesBtn() {
        page.yesBtn.click();
        // Wait for 1 second to ensure the UI is ready after clicking Yes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isTemplateDeletedSuccessfully() {
        // Adjust the selector to match your actual toaster message element
        org.openqa.selenium.By toasterLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'toast') and contains(.,'Template deleted successfully')]");
        try {
            // Wait for the toaster to appear
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 5)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(toasterLocator));
            // Wait for the toaster to disappear
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 5)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(toasterLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 