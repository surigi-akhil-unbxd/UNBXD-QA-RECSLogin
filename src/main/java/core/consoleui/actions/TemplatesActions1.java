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
import com.google.gson.JsonObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class TemplatesActions1 {
    private WebDriver driver;
    private TemplatesPage page;

    public TemplatesActions1(WebDriver driver) {
        this.driver = driver;
        this.page = PageFactory.initElements(driver, TemplatesPage.class);
    }

    // All template action methods copied from TemplatesActions

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
        System.out.println("[DEBUG] About to click the Create button in TemplatesActions1.clickCreateButton()");
        try {
            org.openqa.selenium.WebElement beamer = driver.findElement(org.openqa.selenium.By.id("beamerSelector"));
            if (beamer.isDisplayed()) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", beamer);
            }
        } catch (Exception ignored) {}
        page.createButton.click();
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
        // Wait for the overlay/header to disappear
        org.openqa.selenium.By overlay = org.openqa.selenium.By.className("unbxd-boutique-preview-header");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 30)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(overlay));
        // Scroll into view (if needed)
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", page.applyTemplateBtn);
        // Now click
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
        // Try to find a radio input or label by value or text
        try {
            // Try input by value
            org.openqa.selenium.WebElement radio = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='radio'][value='" + value.toLowerCase() + "']"));
            if (!radio.isSelected()) {
                radio.click();
                return;
            }
        } catch (Exception e) {
            // Fallback: try label by text
            try {
                org.openqa.selenium.WebElement label = driver.findElement(org.openqa.selenium.By.xpath("//label[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + value.toLowerCase() + "') or contains(@for, '" + value.toLowerCase() + "') ]"));
                label.click();
            } catch (Exception ex) {
                System.out.println("[WARN] Could not find radio button or label for value: " + value);
            }
        }
    }

    public void openTemplateCreation() {
        clickCreateTemplateBtn();
        clickCreateNewLink();
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
        // clickProductsInDisplayDropdown();
        checkGlobalAddonsCheckbox();
        checkLikeButtonCheckbox();
        checkDislikeButtonCheckbox();
        checkAddToWishlistButtonCheckbox();
        checkShareButtonCheckbox();
        clickApplyTemplateBtn();
    }

    public void selectTemplateType(String type) {
        // Assumes the dropdown is already open
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + type + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void selectTemplateTypeByIndex(int index) {
        // Find all template type options in the dropdown
        java.util.List<org.openqa.selenium.WebElement> options = driver.findElements(
            org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span")
        );
        if (index >= 0 && index < options.size()) {
            org.openqa.selenium.WebElement option = options.get(index);
            new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(option))
                .click();
        } else {
            System.out.println("[WARN] Invalid index for template type: " + index);
        }
    }

    public void navigateToTemplatesPage() {
        String siteId = EnvironmentConfig.getSiteId();
        String templatesUrl = EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/manage/templates";
        driver.get(templatesUrl);
    }

    public void selectMaximumProducts(String value) {
        // Click the Maximum Products dropdown
        page.maximumProductsDropdown.click();
        // Wait for the dropdown options to be visible and clickable
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + value + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void selectProductsInDisplay(String value) {
        // Click the Products In Display dropdown
        page.productsInDisplayDropdown.click();
        // Wait for the dropdown options to be visible
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + value + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(optionLocator));
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isTemplateDeletedSuccessfully() {
        org.openqa.selenium.By toasterLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'toast') and contains(.,'Template deleted successfully')]");
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

    public void selectMediumAndLayout(String medium, String layout) {
        selectRadioBtn(medium);
        selectRadioBtn(layout);
    }

    public void setWidgetSize(String size) {
        
        page.widgetSizeInput.clear();
        page.widgetSizeInput.sendKeys(size);
    }

    public void setWidgetSizeFromTestData(String size) {
        // Click the widget size input (acts as dropdown)
        page.widgetSizeInput.click();
        page.widgetSizeInput.clear();
        page.widgetSizeInput.sendKeys(size);
    }

    public void selectProductsInDisplayFromTestData(String value) {
        page.productsInDisplayDropdown.click();
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + value + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void clickWidgetSizeDropdown() {
        page.widgetSizeInput.click();
    }

    public void fillTemplateForm(JsonObject dataMap) {
        String medium = dataMap.get("Medium").getAsString();
        String layout = dataMap.get("Layout").getAsString();
        selectMediumAndLayout(medium, layout);

        clickCreateButton();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        String templateName = "autoTemplate" + System.currentTimeMillis();
        System.out.println("[DEBUG] Template name to be entered: " + templateName);
        enterTemplateName(templateName);
        // Optionally, you can add this to dataMap for later use
        dataMap.addProperty("TemplateName", templateName);

        clickTemplateTypeDropdown();
        String templateType = dataMap.get("TemplateType").getAsString();
        System.out.println("[DEBUG] TemplateType: " + templateType);
        selectTemplateType(templateType);

        clickWidgetSizeDropdown();
        String widgetSize = dataMap.get("widgetSize").getAsString();
        System.out.println("[DEBUG] widgetSize: " + widgetSize);
        enterWidgetSize(widgetSize);
        
        // Only select Products In Display if TemplateType is not 'Boutique'
        if (!"Boutique".equalsIgnoreCase(templateType)) {
            selectProductsInDisplay(dataMap.get("ProductsInDisplay").getAsString());
        }
        // Always select Maximum Products if present in data
        if (dataMap.has("MaximumProducts")) {
            selectMaximumProducts(dataMap.get("MaximumProducts").getAsString());
        }
    }

    public void applyTemplateButton() {
        
        clickApplyTemplateBtn();
        clickProceedTemplateBtn();
    }

    public void searchTemplateName(String templateName) {
        searchTemplateByName(templateName);
        hoverOnTemplateName(templateName);
    }

    public boolean isHorizontalWidgetDisplayed() {
        try {
            TemplatesPage templatesPage = new TemplatesPage();
            return templatesPage.horizontalWidget != null && templatesPage.horizontalWidget.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVerticalWidgetDisplayed() {
        try {
            TemplatesPage templatesPage = new TemplatesPage();
            return templatesPage.verticalWidget != null && templatesPage.verticalWidget.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectButtonPosition(String position) {
        // Click the Button Position dropdown
        page.buttonPositionDropdown.click();
        // Wait for the dropdown options to be visible and clickable
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + position + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void clickDemoPreviewButton() {
        page.demoPreviewButton.click();
    }

    public boolean isHorizontalBoutiqueWidgetFrameDisplayed() {
        try {
            return page.horizontalBoutiqueWidgetFrame != null && page.horizontalBoutiqueWidgetFrame.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBackToTemplateCustomization() {
        page.backToTemplateCustomization.click();
    }

    public boolean isMobileFrameDisplayed() {
        try {
            return page.mobileFrame != null && page.mobileFrame.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBehaviourBasedOnDropdown() {
        page.behaviourBasedOnDropdown.click();
    }

    public void selectBehaviourBasedOn(String behaviourType) {
        clickBehaviourBasedOnDropdown();
        // Wait for the dropdown options to be visible and clickable
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + behaviourType + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void swipeTinderProducts() throws InterruptedException {
        // Swipe left (dislike) on the first two products
        for (int i = 0; i < 2 && i < page.tinderProductCards.size(); i++) {
            WebElement dislikeBtn = page.tinderProductCards.get(i).findElement(By.cssSelector(".unbxddislikeablebtn"));
            dislikeBtn.click();
            Thread.sleep(500); // Optional: wait for animation
        }
        // Swipe right (like) on the next two products
        for (int i = 2; i < 4 && i < page.tinderProductCards.size(); i++) {
            WebElement likeBtn = page.tinderProductCards.get(i).findElement(By.cssSelector(".unbxdlikeablebtn"));
            likeBtn.click();
            Thread.sleep(500); // Optional: wait for animation
        }
    }

    public boolean areInstagramBehaviorProductsDisplayed() {
        // Check if the list is not empty and all elements are displayed
        if (page.instagramBehavior == null || page.instagramBehavior.isEmpty()) {
            return false;
        }
        for (WebElement product : page.instagramBehavior) {
            if (!product.isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    public void clearSearchInput() {
        page.searchInput.clear();
    }

    public void clickCreateNewButton() {
        page.createNewLink.click();
    }

    public void clickNextButton() {
        WebElement nextBtn = driver.findElement(By.cssSelector("div.unbxd-side-drawer-footer span"));
        nextBtn.click();
    }

    public void enterRandomTemplateName() {
        String randomName = "autoCusrorTemplate" + System.currentTimeMillis();
        page.enterNameField.clear();
        page.enterNameField.sendKeys(randomName);
    }

    public void clickUploadFromDevice() {
        page.uploadFromDevice.click();
    }

    public void uploadTemplateFile(String fileName) {
        // Example: search in user's Downloads directory
        String userHome = System.getProperty("user.home");
        String downloadsDir = userHome + "/Downloads/";
        java.io.File file = new java.io.File(downloadsDir + fileName);
        if (file.exists()) {
            page.uploadFromDevice.sendKeys(file.getAbsolutePath());
        } else {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
    }

    public void clickProceedBtn() {
        page.proceedBtn.click();
    }
} 