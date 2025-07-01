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

    public String enterRandomExperienceName() {
        String randomStr = java.util.UUID.randomUUID().toString().substring(0, 6);
        String experienceName = "EXP" + randomStr;
        robustSetExperienceName(experienceName);
        return experienceName;
    }

    public void robustSetExperienceName(String experienceName) {
        By inputLocator = By.cssSelector("div.modifier.align-self-center > input[type='text']");
        WebDriverWait wait = new WebDriverWait(driver, 15);
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        for (int attempt = 0; attempt < 5; attempt++) {
            try {
                input.click();
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].removeAttribute('readonly'); arguments[0].removeAttribute('disabled');", input);
                // Debug: log and screenshot before clearing
                System.out.println("[DEBUG] Attempt " + (attempt+1) + ": Before clearing, value='" + input.getAttribute("value") + "'");
                takeDebugScreenshot("before_clearing_attempt_" + attempt);
                // Try JS clear
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input);
                // Try Ctrl+A + Delete
                input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                // Try .clear()
                input.clear();
                Thread.sleep(200);
                // Wait for field to be empty
                for (int w = 0; w < 5; w++) {
                    if (input.getAttribute("value").isEmpty()) break;
                    Thread.sleep(100);
                }
                // Debug: log and screenshot after clearing
                System.out.println("[DEBUG] Attempt " + (attempt+1) + ": After clearing, value='" + input.getAttribute("value") + "'");
                takeDebugScreenshot("after_clearing_attempt_" + attempt);
                if (input.getAttribute("value").isEmpty()) {
                    input.sendKeys(experienceName);
                    wait.until(ExpectedConditions.attributeToBe(input, "value", experienceName));
                    System.out.println("[DEBUG] Experience name set to: " + experienceName);
                    takeDebugScreenshot("after_setting_name");
                    return;
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Exception during clearing attempt: " + e.getMessage());
            }
        }
        // Take a screenshot for debugging
        takeDebugScreenshot("experience_name_not_cleared");
        System.out.println("[ERROR] Final value in experience name field: '" + input.getAttribute("value") + "'");
        throw new RuntimeException("Failed to set experience name after retries");
    }

    public void selectPageByName(String pageName) {
        page.pageDropdown.click();
        By optionLocator = By.xpath("//span[normalize-space()='" + pageName + "']");
        WebElement option = driver.findElement(optionLocator);
        option.click();
    }

    public void selectWidgetByName(String widgetName) {
        page.widgetDropdown.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        By optionLocator = By.xpath("//span[normalize-space()='" + widgetName + "']");
        WebElement option = driver.findElement(optionLocator);
        option.click();
    }

    public void clickTemplateTypeDropdown() {
        page.templateSearchInput.click();
    }

    public void clickCustomAlgoDropdown() {
        page.algoDropdown.click();
    }

    public void selectTemplateByName(String templateName) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(page.templateSearchInput));
        page.templateSearchInput.click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector("ul.optionContainer li.option")
        ));
        List<WebElement> allOptions = driver.findElements(By.cssSelector("ul.optionContainer li.option"));
        for (WebElement opt : allOptions) {
            if (opt.getText().replaceAll("\\s+", " ").trim().equalsIgnoreCase(templateName.replaceAll("\\s+", " ").trim())) {
                opt.click();
                return;
            }
        }
    }

    public void selectCustomAlgo(String customAlgo) {
        page.algoDropdown.click();
        String xpath = "//span[normalize-space()='" + customAlgo.trim() + "']";
        List<WebElement> options = driver.findElements(By.xpath(xpath));
        if (options.isEmpty()) {
            
            List<WebElement> allOptions = driver.findElements(By.cssSelector("span.primary-label"));
            System.out.println("[ERROR] Custom algorithm '" + customAlgo + "' not found. Available options:");
            for (WebElement opt : allOptions) {
                System.out.println(" - " + opt.getText());
            }
            takeDebugScreenshot("custom_algo_not_found");

            page.algoDropdown.click();
            options = driver.findElements(By.xpath(xpath));
            if (options.isEmpty()) {
                throw new NoSuchElementException("Custom algorithm '" + customAlgo + "' not found after self-healing.");
            }
        }
        options.get(0).click();
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

    // Utility methods for debug
    private void takeDebugScreenshot(String name) {
        try {
            org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
            byte[] screenshotBytes = ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            java.io.File dest = new java.io.File("./target/screenshots/" + name + "_" + System.currentTimeMillis() + ".png");
            dest.getParentFile().mkdirs();
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                fos.write(screenshotBytes);
            }
            System.out.println("Screenshot saved at: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to take screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void saveDebugHtml(String filename) {
        try {
            String html = driver.getPageSource();
            java.nio.file.Files.write(java.nio.file.Paths.get("./target/screenshots/" + filename), html.getBytes());
            System.out.println("HTML saved at: ./target/screenshots/" + filename);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to save HTML: " + e.getMessage());
        }
    }

    // Utility: Close overlays if present
    public void closeOverlaysIfPresent() {
        List<WebElement> overlays = driver.findElements(By.cssSelector(".modal-backdrop, .dropdown-backdrop, .loading-overlay, .spinner-overlay"));
        for (WebElement overlay : overlays) {
            if (overlay.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", overlay);
            }
        }
    }

    // Utility: Select dropdown option robustly
    public void selectDropdownOptionByText(WebElement dropdown, String optionText) {
        dropdown.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("span.primary-label")));
        List<WebElement> options = driver.findElements(By.cssSelector("span.primary-label"));
        boolean found = false;
        for (WebElement opt : options) {
            if (opt.getText().trim().equalsIgnoreCase(optionText.trim())) {
                opt.click();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("[ERROR] Option '" + optionText + "' not found. Available options:");
            for (WebElement opt : options) {
                System.out.println(" - " + opt.getText());
            }
            takeDebugScreenshot("dropdown_option_not_found");
            throw new NoSuchElementException("Option '" + optionText + "' not found in dropdown.");
        }
    }

    public WebElement getExperienceNameInput() {
        return page.experienceNameInput;
    }
} 