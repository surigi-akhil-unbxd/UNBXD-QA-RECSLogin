package core.consoleui.actions;

import core.consoleui.pages.CustomAlgorithmsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import lib.EnvironmentConfig;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class CustomAlgorithmsActions {
    private WebDriver driver;
    private CustomAlgorithmsPage page;
    private String CustomAlgoName;

    public CustomAlgorithmsActions(WebDriver driver) {
        this.driver = driver;
        this.page = PageFactory.initElements(driver, CustomAlgorithmsPage.class);
    }

    // Add your custom algorithm action methods here

    public void navigateToRecsPage() {
        // TODO: Add navigation logic to the Recs page
        // Example: driver.get("<recs-page-url>");
    }

    public void navigateToCustomAlgorithmsPage() {
        String siteId = EnvironmentConfig.getSiteId();
        String customAlgosUrl = EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/manage/custom-algos";
        driver.get(customAlgosUrl);
    }

    public void clickCreateNewButton() {
        page.createNewButton.click();
    }

    public void selectAlgorithmByName(String customAlgo) {
        page.algorithmSelectorDropdownToggle.click();
        // Wait for the dropdown options to be visible and clickable
        org.openqa.selenium.By optionLocator = org.openqa.selenium.By.xpath("//div[contains(@class,'dropdown-menu')]//span[normalize-space()='" + customAlgo + "']");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, 10)
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(optionLocator))
            .click();
    }

    public void clickMinimizeTitle() {
        page.minimizeTitle.click();
    }

    public void clickSaveButton() {
        page.saveButton.click();
    }

    public void searchCustomAlgorithmByName(String algoName) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 20);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(page.searchCustomAlgorithmInput));
        page.searchCustomAlgorithmInput.click();
        // Use JS to set value and trigger input event
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
            page.searchCustomAlgorithmInput, algoName
        );
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        page.searchCustomAlgorithmInput.sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    public boolean isAlgorithmPresentInListing(String algoName) {
        for (org.openqa.selenium.WebElement el : page.algorithmNameList) {
            if (el.getText().trim().equalsIgnoreCase(algoName.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Robustly clears the name field using JS, Ctrl+A+Delete, and .clear(), then enters the new name.
     */
    public void robustClearAndEnterName(String newName) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 20);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(page.enterNameField));
        page.enterNameField.click();

        // Try up to 3 times to clear and set the value
        for (int attempt = 0; attempt < 3; attempt++) {
            // Clear using JS
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].value = '';", page.enterNameField);
            // Ctrl+A (select all) as a string chord, then Delete
            page.enterNameField.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"));
            page.enterNameField.sendKeys(org.openqa.selenium.Keys.DELETE);
            // Fallback to .clear()
            page.enterNameField.clear();
            // Enter the new name
            page.enterNameField.sendKeys(newName);
            // Trigger input event
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", page.enterNameField);

            // Wait a bit for UI to react
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}

            // Check if the value is set correctly
            String currentValue = page.enterNameField.getAttribute("value");
            if (newName.equals(currentValue)) {
                break;
            }
            // Optionally, log a warning if not set
            if (attempt == 2) {
                System.out.println("Warning: Name field value after 3 attempts is: " + currentValue);
            }
        }
    }

    public String enterRandomCursorAlgoName() {
        CustomAlgoName = "cursorAlgo" + System.currentTimeMillis();
        robustClearAndEnterName(CustomAlgoName);
        return CustomAlgoName;
    }

    public void clickEditButton() {
        // Wait for the edit button to be present and clickable
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 20);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(page.editButton));
        // Take debug screenshot before clicking edit
        takeDebugScreenshot("before_edit_click");
        page.editButton.click();
    }

    public void clickDeleteButton() {
        page.deleteButton.click();
    }

    public void hoverAndClickOnCreatedAlgorithm() {
        java.util.List<org.openqa.selenium.WebElement> algoElements = driver.findElements(org.openqa.selenium.By.cssSelector(".item-name"));
        for (org.openqa.selenium.WebElement el : algoElements) {
            if (el.getText().trim().equalsIgnoreCase(CustomAlgoName.trim())) {
                org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                actions.moveToElement(el).click().perform();
                break;
            }
        }
    }

    public void clickProceedButton() {
        page.ProceedButton.click();
    }

    public void setLookBackPeriod(int lookbackPeriod) {
        page.lookBackPeriodDropdown.clear();
        page.lookBackPeriodDropdown.sendKeys(String.valueOf(lookbackPeriod));
    }

    public void clearSearchCustomAlgorithmInput() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(page.searchCustomAlgorithmInput));
        page.searchCustomAlgorithmInput.click();
        page.searchCustomAlgorithmInput.clear();
        page.searchCustomAlgorithmInput.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"));
        page.searchCustomAlgorithmInput.sendKeys(org.openqa.selenium.Keys.DELETE);
    }

    private void takeDebugScreenshot(String step) {
        try {
            String fileName = "./target/screenshots/debug_" + step + "_" + System.currentTimeMillis() + ".png";
            byte[] screenshotBytes = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            java.io.File dest = new java.io.File(fileName);
            dest.getParentFile().mkdirs();
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                fos.write(screenshotBytes);
            }
            System.out.println("Debug screenshot saved at: " + fileName);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to take debug screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}