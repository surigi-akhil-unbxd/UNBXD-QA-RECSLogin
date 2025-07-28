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
import org.openqa.selenium.support.ui.ExpectedCondition;

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
        // AGGRESSIVE popup handling before attempting click
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.createTemplateBtn.click();
            System.out.println("[DEBUG] Create Template button clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.createTemplateBtn);
                System.out.println("[DEBUG] Create Template button clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
    }

    public void clickFiltersBtn() {
        page.filtersBtn.click();
    }

    public void clickCreateNewLink() {
        // AGGRESSIVE popup handling before attempting click
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.createNewLink.click();
            System.out.println("[DEBUG] Create New Link clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.createNewLink);
                System.out.println("[DEBUG] Create New Link clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
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
        System.out.println("[DEBUG] Clicking Create button");
        
        // AGGRESSIVE popup handling before attempting click
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.createButton.click();
            System.out.println("[DEBUG] Create button clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.createButton);
                System.out.println("[DEBUG] Create button clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
    }

    public void enterTemplateName(String name) {
        System.out.println("[DEBUG] Entering template name: " + name);
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(page.templateNameInput));
        wait.until(ExpectedConditions.elementToBeClickable(page.templateNameInput));
        
        // Click to focus the field
        page.templateNameInput.click();
        
        // Clear using multiple strategies to ensure it's completely cleared
        try {
            // Strategy 1: JavaScript clear
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", page.templateNameInput);
            System.out.println("[DEBUG] Cleared template name using JavaScript");
        } catch (Exception e) {
            System.out.println("[WARN] JavaScript clear failed: " + e.getMessage());
        }
        
        try {
            // Strategy 2: Select all and delete
            page.templateNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            page.templateNameInput.sendKeys(Keys.DELETE);
            System.out.println("[DEBUG] Cleared template name using Ctrl+A + Delete");
        } catch (Exception e) {
            System.out.println("[WARN] Ctrl+A clear failed: " + e.getMessage());
        }
        
        try {
            // Strategy 3: Move to end and backspace multiple times
            page.templateNameInput.sendKeys(Keys.END);
            for (int i = 0; i < 50; i++) {
                page.templateNameInput.sendKeys(Keys.BACK_SPACE);
            }
            System.out.println("[DEBUG] Cleared template name using backspace");
        } catch (Exception e) {
            System.out.println("[WARN] Backspace clear failed: " + e.getMessage());
        }
        
        // Strategy 4: Force clear any remaining text with JavaScript
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = ''; " +
                "arguments[0].innerHTML = ''; " +
                "arguments[0].textContent = '';", 
                page.templateNameInput
            );
            System.out.println("[DEBUG] Force cleared template name using aggressive JavaScript");
        } catch (Exception e) {
            System.out.println("[WARN] Force clear failed: " + e.getMessage());
        }
        
        // Verify the field is empty
        String currentValue = page.templateNameInput.getAttribute("value");
        if (currentValue != null && !currentValue.trim().isEmpty()) {
            System.out.println("[WARN] Template name field still contains: '" + currentValue + "'");
            // Try one more time with different approach
            try {
                page.templateNameInput.clear();
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", page.templateNameInput);
            } catch (Exception e) {
                System.out.println("[WARN] Final clear attempt failed: " + e.getMessage());
            }
        } else {
            System.out.println("[DEBUG] Template name field cleared successfully");
        }
        
        // Enter the new name
        page.templateNameInput.sendKeys(name);
        System.out.println("[DEBUG] Entered template name: " + name);
        
        // Verify the new name was entered
        String finalValue = page.templateNameInput.getAttribute("value");
        System.out.println("[DEBUG] Final template name value: '" + finalValue + "'");
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
        System.out.println("[DEBUG] Clicking Apply Template button");
        page.applyTemplateBtn.click();
        System.out.println("[DEBUG] Apply Template button clicked");
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
        System.out.println("[DEBUG] Clicking radio button for: " + value);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            if (value.equalsIgnoreCase("Desktop")) {
                page.desktopRadioBtn.click();
            } else if (value.equalsIgnoreCase("Mobile")) {
                page.mobileRadioBtn.click();
            } else if (value.equalsIgnoreCase("Horizontal")) {
                page.horizontalRadioBtn.click();
            } else if (value.equalsIgnoreCase("Vertical")) {
                page.verticalRadioBtn.click();
            }
            System.out.println("[DEBUG] Radio button clicked normally for: " + value);
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                // JavaScript click based on value
                if (value.equalsIgnoreCase("Desktop")) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.desktopRadioBtn);
                } else if (value.equalsIgnoreCase("Mobile")) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.mobileRadioBtn);
                } else if (value.equalsIgnoreCase("Horizontal")) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.horizontalRadioBtn);
                } else if (value.equalsIgnoreCase("Vertical")) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.verticalRadioBtn);
                }
                System.out.println("[DEBUG] Radio button clicked using JavaScript for: " + value);
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
        
        System.out.println("[DEBUG] Clicked radio button for: " + value);
    }

    public void openTemplateCreation() {
        clickCreateTemplateBtn();
        clickCreateNewLink();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.cssSelector("label.unbxd-card-select[for='desktop']")
            ));
        // Handle popups after opening template creation
        handleAllPopupsImmediately();
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
        
        // Immediately handle any popups that might appear after navigation
        handleAllPopupsImmediately();
    }
    
    public void handleAllPopupsImmediately() {
        // Use the utility class for consistent popup handling - same as ExpActions
        core.utils.PopupHandler.handleAllPopupsImmediately(driver);
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
        // OPTIMIZATION: Simple search without unnecessary popup handling
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
        // AGGRESSIVE popup handling before attempting hover
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        org.openqa.selenium.By templateNameLocator = org.openqa.selenium.By.xpath("//*[contains(text(), '" + name + "')]");
        org.openqa.selenium.WebElement element = new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(templateNameLocator));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        
        // Additional popup handling after hover to prevent interference with subsequent clicks
        core.utils.PopupHandler.handleAllPopupsImmediately(driver);
    }

    public void clickEditTemplateBtn() {
        System.out.println("[DEBUG] Clicking Edit Template button");
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.editTemplateBtn.click();
            System.out.println("[DEBUG] Edit Template button clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.editTemplateBtn);
                System.out.println("[DEBUG] Edit Template button clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
    }

    public void clickDeleteTemplateBtn() {
        // AGGRESSIVE popup handling before attempting click
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.deleteTemplateBtn.click();
            System.out.println("[DEBUG] Delete Template button clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.deleteTemplateBtn);
                System.out.println("[DEBUG] Delete Template button clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
    }

    public void clickConfirmDeleteBtn() {
        // AGGRESSIVE popup handling before attempting click
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Try normal click first, if it fails, use JavaScript
        try {
            page.confirmDeleteBtn.click();
            System.out.println("[DEBUG] Confirm Delete button clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click: " + e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.confirmDeleteBtn);
                System.out.println("[DEBUG] Confirm Delete button clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[ERROR] JavaScript click also failed: " + jsEx.getMessage());
                throw jsEx;
            }
        }
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
        System.out.println("[DEBUG] Starting selectMediumAndLayout - Medium: " + medium + ", Layout: " + layout);
        long startTime = System.currentTimeMillis();
        
        selectRadioBtn(medium);
        long mediumTime = System.currentTimeMillis();
        System.out.println("[DEBUG] Medium selection completed in " + (mediumTime - startTime) + "ms");
        
        selectRadioBtn(layout);
        long layoutTime = System.currentTimeMillis();
        System.out.println("[DEBUG] Layout selection completed in " + (layoutTime - mediumTime) + "ms");
        
        System.out.println("[DEBUG] Total selectMediumAndLayout time: " + (layoutTime - startTime) + "ms");
        
        // AGGRESSIVE popup handling before Create button click
        System.out.println("[DEBUG] Aggressively handling popups before Create button click");
        core.utils.PopupHandler.aggressivelyRemoveAllPopupsBeforeClick(driver);
        core.utils.PopupHandler.specificallyRemovePushActionsDiv(driver);
        
        // Immediately click Create button after layout selection
        System.out.println("[DEBUG] Immediately clicking Create button after layout selection");
        clickCreateButton();
    }
    
    public void clickCreateButtonImmediately() {
        System.out.println("[DEBUG] Immediately clicking Create button");
        
        // Hide beamerSelector immediately
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var beamer = document.getElementById('beamerSelector'); " +
                "if (beamer) beamer.style.display = 'none';"
            );
        } catch (Exception ignored) {}
        
        // Quick popup handling
        handleAllPopupsImmediately();
        
        // Click immediately without waiting
        try {
            page.createButton.click();
            System.out.println("[DEBUG] Create button clicked immediately");
        } catch (Exception e) {
            System.out.println("[WARN] Immediate click failed, trying JavaScript: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.createButton);
        }
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

        // Removed unnecessary 1-second delay - form is now ready from clickCreateButton

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
        page.searchInput.clear();
        page.searchInput.sendKeys(templateName);
        // Wait for the search input value to match (ensures input is stable)
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 3)
            .until((ExpectedCondition<Boolean>) d -> page.searchInput.getAttribute("value").equals(templateName));
        // Optionally, wait for either the template to appear or for a 'no results' state
        // (If you have a 'no results' element, add a wait here)
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

    public void handleNoThanksPopupImmediately() {
        // Use the centralized popup handler like ExpActions for consistency
        core.utils.PopupHandler.handleAllPopupsImmediately(driver);
        System.out.println("Handled No Thanks popup using centralized popup handler");
    }
    
    public void clickNoThanksButton() {
        // Use the centralized popup handler like ExpActions
        core.utils.PopupHandler.handleAllPopupsImmediately(driver);
        System.out.println("Clicked No Thanks button using centralized popup handler");
    }
    
    /**
     * Verify that a template is not present in the current search results.
     * This method checks the current search results without re-searching.
     * 
     * @param templateName The name of the template to verify is not present
     * @return true if template is not found, false if template is still present
     */
    public boolean isTemplateNotPresentInListing(String templateName) {
        try {
            System.out.println("[DEBUG] Verifying template '" + templateName + "' is not present in current search results");
            org.openqa.selenium.By templateLocator = org.openqa.selenium.By.xpath(
                "//div[contains(@class,'template-item') or contains(@class,'template-card')]//span[contains(text(),'" + templateName + "')]"
            );
            // Wait up to 3 seconds for the template to disappear
            boolean notPresent = new org.openqa.selenium.support.ui.WebDriverWait(driver, 3, 100)
                .until((ExpectedCondition<Boolean>) d -> d.findElements(templateLocator).isEmpty());
            if (notPresent) {
                System.out.println("[DEBUG] Template '" + templateName + "' is NOT present in listing - verification successful");
                return true;
            } else {
                System.out.println("[WARN] Template '" + templateName + "' is still present in listing - verification failed");
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error verifying template absence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Quick verification method to ensure template is completely deleted from listing.
     * This method verifies deletion and throws exception if template still exists.
     * 
     * @param templateName The name of the template to verify deletion
     * @throws RuntimeException if template is still present in listing
     */
    public void verifyTemplateDeletionComplete(String templateName) {
        boolean templateNotPresent = isTemplateNotPresentInListing(templateName);
        if (!templateNotPresent) {
            throw new RuntimeException("Template '" + templateName + "' is still present in listing after deletion");
        }
        System.out.println("[DEBUG] Template deletion verification completed successfully");
    }
} 