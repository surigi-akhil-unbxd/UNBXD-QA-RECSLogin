package core.consoleui.actions;

import core.consoleui.pages.ExpPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Keys;

import java.util.List;
import java.util.ArrayList;

public class ExpActions {
    private WebDriver driver;
    private ExpPage page;

    public ExpActions(WebDriver driver) {
        this.driver = driver;
        this.page = PageFactory.initElements(driver, ExpPage.class);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
    }
   

    public void closeNoThanksPopupIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement noThanksBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("pushActionRefuse")));
            if (noThanksBtn.isDisplayed()) {
                noThanksBtn.click();
                System.out.println("Closed 'No Thanks' popup.");
            }
        } catch (Exception ignored) {
            // Popup not present, nothing to close
        }
    }

    public void closePopupIfPresent() {
        if (!driver.findElements(By.xpath("//div[@class=\"modal-content\"]//div[@class=\"modal-header\"]//button")).isEmpty()) {
            WebElement closeBtn = driver.findElement(By.xpath("//div[@class=\"modal-content\"]//div[@class=\"modal-header\"]//button"));
            if (closeBtn.isDisplayed()) {
                closeBtn.click();
            }
        }
    }

    public void handleAllPopups() {
        closeNoThanksPopupIfPresent();
        closePopupIfPresent();
    }

    public void navigateToExperiencePage() {
        String siteId = lib.EnvironmentConfig.getSiteId();
        String experienceUrl = lib.EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/experience";
        driver.get(experienceUrl);
    }

    public void clickCreateExperienceBtn() {
        page.createExperienceBtn.click();
    }

    public void clearExperienceNameField() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(page.experienceNameInput));
        page.experienceNameInput.click();
        // Move cursor to end (cross-platform)
        page.experienceNameInput.sendKeys(Keys.END);
        page.experienceNameInput.sendKeys(Keys.chord(Keys.COMMAND, Keys.ARROW_RIGHT));
        // Now send many BACKSPACEs
        for (int i = 0; i < 30; i++) {
            page.experienceNameInput.sendKeys(Keys.BACK_SPACE);
        }
    }

    public String enterRandomExperienceName() {
        String randomStr = java.util.UUID.randomUUID().toString().substring(0, 6);
        String experienceName = "EXP" + randomStr;
        clearExperienceNameField();
        page.experienceNameInput.sendKeys(experienceName);
        return experienceName;
    }

    public void selectPageByName(String pageName) {
        page.pageDropdown.click();
        By optionLocator = By.xpath("//span[normalize-space()='" + pageName + "']");
        WebElement option = driver.findElement(optionLocator);
        option.click();
    }

    public void selectWidgetByName(String widgetName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        System.out.println("[DEBUG] Attempting to click widget dropdown...");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(page.widgetDropdown));
            page.widgetDropdown.click();
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JS click on widget dropdown");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", page.widgetDropdown);
        }
        // Wait for dropdown to open (look for options to appear)
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a.dropdown-item")));
        // Re-fetch dropdown items after opening
        List<WebElement> dropdownItems = driver.findElements(By.cssSelector("a.dropdown-item"));
        System.out.println("[DEBUG] Available widget options:");
        for (WebElement item : dropdownItems) {
            System.out.println("[DEBUG] Option: '" + item.getText() + "'");
            if (item.getText().trim().equals(widgetName.trim())) {
                try {
                    item.click();
                } catch (Exception e) {
                    System.out.println("[WARN] Normal click failed, trying JS click on widget option");
                    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", item);
                }
                System.out.println("[DEBUG] Clicked widget: '" + widgetName + "'");
                return;
            }
        }
        throw new NoSuchElementException("Widget '" + widgetName + "' not found in dropdown");
    }

    public void clickTemplateTypeDropdown() {
        page.templateSearchInput.click();
    }

    public void clickCustomAlgoDropdown() {
        page.algoDropdown.click();
    }

    public void selectTemplateByName(String templateName) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        
        // Wait for template search input to be clickable
        wait.until(ExpectedConditions.elementToBeClickable(page.templateSearchInput));
        page.templateSearchInput.click();
        
        // Enhanced wait for template options with multiple strategies
        waitForTemplateOptionsToLoad();
        
        // Try multiple selectors for template options
        List<WebElement> allOptions = findTemplateOptionsWithFallback();
        
        if (allOptions.isEmpty()) {
            throw new RuntimeException("No template options found after waiting");
        }
        
        // Find and click the matching template
        boolean templateFound = false;
        for (WebElement opt : allOptions) {
            try {
                String optionText = opt.getText().replaceAll("\\s+", " ").trim();
                String targetText = templateName.replaceAll("\\s+", " ").trim();
                
                if (optionText.equalsIgnoreCase(targetText)) {
                    // Handle stale element reference
                    opt = handleStaleElement(opt, By.cssSelector("ul.optionContainer li.option, .dropdown-menu li, .option-list li"));
                    opt.click();
                    templateFound = true;
                    System.out.println("[DEBUG] Selected template: " + templateName);
                    break;
                }
            } catch (Exception e) {
                System.out.println("[WARN] Error processing template option: " + e.getMessage());
                continue;
            }
        }
        
        if (!templateFound) {
            throw new RuntimeException("Template '" + templateName + "' not found in available options");
        }
    }
    
    /**
     * Enhanced method to find template options with fallback selectors
     */
    private List<WebElement> findTemplateOptionsWithFallback() {
        String[] selectors = {
            "ul.optionContainer li.option",
            ".dropdown-menu li",
            ".option-list li",
            "[class*='option'] li",
            "[class*='dropdown'] li"
        };
        
        for (String selector : selectors) {
            try {
                List<WebElement> options = driver.findElements(By.cssSelector(selector));
                if (!options.isEmpty()) {
                    System.out.println("[DEBUG] Found template options using selector: " + selector);
                    return options;
                }
            } catch (Exception ignored) {}
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Enhanced wait for template options to load
     */
    private void waitForTemplateOptionsToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        
        // Wait for any loading indicators to disappear first
        try {
            List<WebElement> loadingElements = driver.findElements(By.cssSelector(".loading, .spinner, .loader, [class*='loading'], [class*='spinner']"));
            if (!loadingElements.isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOfAllElements(loadingElements));
            }
        } catch (Exception ignored) {}
        
        // Wait for template options to appear with multiple selectors
        boolean optionsLoaded = false;
        String[] selectors = {
            "ul.optionContainer li.option",
            ".dropdown-menu li",
            ".option-list li"
        };
        
        for (String selector : selectors) {
            try {
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selector)));
                optionsLoaded = true;
                System.out.println("[DEBUG] Template options loaded using selector: " + selector);
                break;
            } catch (Exception ignored) {}
        }
        
        if (!optionsLoaded) {
            System.out.println("[WARN] Template options did not load within timeout");
        }
    }

    public void selectCustomAlgo(String customAlgo) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        // Wait for algorithm dropdown to be clickable
        wait.until(ExpectedConditions.elementToBeClickable(page.algoDropdown));
        page.algoDropdown.click();
        
        // Wait for dropdown options to appear
        waitForAlgorithmOptionsToLoad();
        
        // Try multiple strategies to find and select the algorithm
        boolean algorithmFound = false;
        
        // Strategy 1: Primary XPath locator
        try {
            String xpath = "//span[normalize-space()='" + customAlgo.trim() + "']";
            By optionLocator = By.xpath(xpath);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            option.click();
            algorithmFound = true;
            System.out.println("[DEBUG] Selected algorithm using primary XPath: " + customAlgo);
        } catch (Exception e) {
            System.out.println("[DEBUG] Primary XPath strategy failed for algorithm: " + customAlgo);
        }
        
        // Strategy 2: Alternative XPath with contains
        if (!algorithmFound) {
            try {
                String xpath = "//span[contains(text(), '" + customAlgo.trim() + "')]";
                By optionLocator = By.xpath(xpath);
                WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
                option.click();
                algorithmFound = true;
                System.out.println("[DEBUG] Selected algorithm using contains XPath: " + customAlgo);
            } catch (Exception e) {
                System.out.println("[DEBUG] Contains XPath strategy failed for algorithm: " + customAlgo);
            }
        }
        
        // Strategy 3: Find all options and match text
        if (!algorithmFound) {
            try {
                List<WebElement> allOptions = driver.findElements(By.cssSelector(".dropdown-menu li, .option-list li, [class*='dropdown'] li"));
                for (WebElement opt : allOptions) {
                    String optionText = opt.getText().trim();
                    if (optionText.equalsIgnoreCase(customAlgo.trim())) {
                        opt = handleStaleElement(opt, By.cssSelector(".dropdown-menu li, .option-list li, [class*='dropdown'] li"));
                        opt.click();
                        algorithmFound = true;
                        System.out.println("[DEBUG] Selected algorithm using text matching: " + customAlgo);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Text matching strategy failed for algorithm: " + customAlgo);
            }
        }
        
        if (!algorithmFound) {
            throw new RuntimeException("Custom algorithm '" + customAlgo + "' not found in available options");
        }
    }
    
    /**
     * Enhanced wait for algorithm options to load
     */
    private void waitForAlgorithmOptionsToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        // Wait for any loading indicators to disappear first
        try {
            List<WebElement> loadingElements = driver.findElements(By.cssSelector(".loading, .spinner, .loader, [class*='loading'], [class*='spinner']"));
            if (!loadingElements.isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOfAllElements(loadingElements));
            }
        } catch (Exception ignored) {}
        
        // Wait for dropdown options to appear
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".dropdown-menu li, .option-list li, [class*='dropdown'] li")
            ));
        } catch (Exception e) {
            System.out.println("[WARN] Algorithm options did not load within timeout");
        }
    }
    
    /**
     * Enhanced method to handle stale element reference
     */
    private WebElement handleStaleElement(WebElement element, By locator) {
        try {
            // Try to interact with the element
            element.isDisplayed();
            return element;
        } catch (Exception e) {
            // Element is stale, re-find it
            System.out.println("[DEBUG] Element is stale, re-finding...");
            WebDriverWait wait = new WebDriverWait(driver, 5);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        }
    }

    public void clickSaveButton() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", page.saveBtn);
        page.saveBtn.click();
    }

    public void waitForAndClickExperienceInList(String experienceName) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        String xpath = "//*[normalize-space()='" + experienceName.trim() + "']";
        By expLocator = By.xpath(xpath);
        WebElement expElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(expLocator)
        );
        expElement.click();
    }

    public void clickDeleteExperienceButton() {
        page.deleteExpButton.click();
    }

    public void clickEditExpButton() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement editBtn = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.list-crud-items .icon-edit")
            )
        );
        editBtn.click();
    }

    public void deleteExperience(String experienceName) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        page.deleteExpButton.click();
        By confirmBtnSelector = By.cssSelector("button.btn-primary, button.confirm");
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmBtnSelector));
        confirmBtn.click();
        By expLocator = By.xpath("//*[normalize-space()='" + experienceName.trim() + "']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(expLocator));
    }

    public void clickConfirmDeleteButton() {
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Wait for the modal footer to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-footer")));

        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                WebElement modalFooter = driver.findElement(By.cssSelector(".modal-footer"));
                List<WebElement> buttons = modalFooter.findElements(By.cssSelector("button.btn.btn-primary"));
                for (WebElement btn : buttons) {
                    if (btn.isDisplayed() && btn.isEnabled() && btn.getText().trim().equalsIgnoreCase("YES")) {
                        System.out.println("[LOG] Clicking on YES button in delete confirmation modal.");
                        wait.until(ExpectedConditions.elementToBeClickable(btn));
                        btn.click();
                        System.out.println("Clicked confirm delete button: '" + btn.getText() + "'");
                        // Wait for the modal to disappear after clicking
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-footer")));
                        return;
                    }
                }
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (attempt == 1) throw e;
                System.out.println("[WARN] StaleElementReferenceException, retrying click on YES button...");
            } catch (org.openqa.selenium.NoSuchElementException e) {
                // If modal-footer is gone after click, treat as success
                System.out.println("[INFO] Modal footer not found after click, assuming modal closed successfully.");
                return;
            }
        }
        throw new NoSuchElementException("No visible and enabled YES button found in modal-footer.");
    }

    public void searchExperienceByName(String experienceName) {
        
        page.searchExpInput.sendKeys(experienceName);
    }

    public boolean isExperienceNotPresentInList(String experienceName) {
        try {
            driver.findElement(By.xpath("//*[normalize-space()='" + experienceName.trim() + "']"));
            return false; // Found
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return true; // Not found
        }
    }

    public void clickAdvancedFilterBtn() {
        page.advancedFilterBtn.click();
    }

    public void clickAdvancedFilterDropdown() {
        page.advancedFilterDropdown.click();
    }

    public void selectAdvancedFilterDropdownOption(String optionText) {
        clickAdvancedFilterDropdown();
        for (WebElement option : page.advancedFilterDropdownOptions) {
            if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {
                option.click();
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Option '" + optionText + "' not found in advanced filter dropdown");
    }

    public void clickSelectDropdownBtn() {
        page.selectDropdownBtn.click();
    }

    public void selectDropdownOption(String optionText) {
        clickSelectDropdownBtn();
        for (WebElement option : page.selectDropdownOptions) {
            if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {
                option.click();
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Option '" + optionText + "' not found in select dropdown");
    }

    public void clickAddIconBtn() {
        page.addIconBtn.click();
    }

    public void clickResetAndRemoveBtn() {
        page.resetAndRemoveBtn.click();
    }

    public void clickAdvancedFilterLink() {
        page.advancedFilterLink.click();
    }

    public WebElement getExperienceNameInput() {
        return page.experienceNameInput;
    }
} 