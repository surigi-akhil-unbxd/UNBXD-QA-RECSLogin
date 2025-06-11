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
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(page.widgetDropdown));
        page.widgetDropdown.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a.dropdown-item")));
        List<WebElement> dropdownItems = driver.findElements(By.cssSelector("a.dropdown-item"));
        System.out.println("Available widget options:");
        for (WebElement item : dropdownItems) {
            System.out.println("'" + item.getText() + "'");
            if (item.getText().trim().equals(widgetName.trim())) {
                try {
                    item.click();
                } catch (Exception e) {
                    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", item);
                }
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
        By optionLocator = By.xpath(xpath);
        WebElement option = driver.findElement(optionLocator);
        option.click();
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
} 