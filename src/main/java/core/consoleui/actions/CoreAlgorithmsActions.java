package core.consoleui.actions;

import core.consoleui.pages.CoreAlgorithmsPage;
import org.openqa.selenium.WebDriver;
import lib.EnvironmentConfig;

public class CoreAlgorithmsActions {
    private WebDriver driver;
    private CoreAlgorithmsPage coreAlgorithmsPage;

    public CoreAlgorithmsActions(WebDriver driver) {
        this.driver = driver;
        this.coreAlgorithmsPage = new CoreAlgorithmsPage(driver);
    }

    public void clickCreateNewButton() {
        coreAlgorithmsPage.createNewButton.click();
    }

    public void enterAlgorithmName(String name) {
        coreAlgorithmsPage.algorithmNameInput.clear();
        coreAlgorithmsPage.algorithmNameInput.sendKeys(name);
    }

    
   

    public void navigateToCoreAlgorithmsPage() {
        String siteId = EnvironmentConfig.getSiteId();
        String coreAlgosUrl = EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/manage/core-algos";
        driver.get(coreAlgosUrl);
    }

    public void navigateToCustomAlgorithmsPage() {
        String siteId = EnvironmentConfig.getSiteId();
        String customAlgosUrl = EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + siteId + "/manage/custom-algos";
        driver.get(customAlgosUrl);
    }

    public void clickCoreAlgorithms() {
        coreAlgorithmsPage.coreAlgorithms.click();
    }

    public void clickMinimizeTitle() {
        coreAlgorithmsPage.minimizeTitle.click();
    }

    public void clickCTLConfigureButton() {
        coreAlgorithmsPage.CTLconfigureButton.click();
    }

    public void clickCreateButton() {
        coreAlgorithmsPage.createButton.click();
    }

    public void enterProductId(String productId) {
        coreAlgorithmsPage.enterProductIdInput.click();
        coreAlgorithmsPage.enterProductIdInput.clear();
        coreAlgorithmsPage.enterProductIdInput.sendKeys(productId);
    }

    public void enterChildProductId(String childProductId) {
        coreAlgorithmsPage.enterChildIdInput.click();
        coreAlgorithmsPage.enterChildIdInput.clear();
        coreAlgorithmsPage.enterChildIdInput.sendKeys(childProductId);
    }

    public void enterChildProductId1(String childProductId) {
        org.openqa.selenium.WebElement input = driver.findElement(org.openqa.selenium.By.cssSelector("div:nth-child(4) div:nth-child(1) div:nth-child(1) div:nth-child(1) input:nth-child(1)"));
        input.click();
        input.clear();
        input.sendKeys(childProductId);
    }

    public void clickSaveButton() {
        // Wait for the save button to be clickable before clicking
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 10);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(coreAlgorithmsPage.saveButton));
        coreAlgorithmsPage.saveButton.click();
    }

    public void clickParentIdInListing(String parentId) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 10);
        String xpath = "//table//td[contains(text(),'" + parentId + "')]";
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                org.openqa.selenium.WebElement el = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        org.openqa.selenium.By.xpath(xpath)
                    )
                );
                el.click();
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (attempt == 1) throw e; // Only retry once
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("ParentID '" + parentId + "' not found in listing");
    }

    public void clickEditIcon() {
        coreAlgorithmsPage.editIcon.click();
    }

    public void clickDeleteIcon() {
        coreAlgorithmsPage.deleteIcon.click();
    }

    public void clickProceedButton() {
        // Wait for modal overlay to disappear before clicking Proceed
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, 10);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector(".modal.show")
        ));
        coreAlgorithmsPage.ProceedButton.click();
    }

    public void clickAddButton() {
        coreAlgorithmsPage.addButton.click();
    }

    public void enterProductIdInSearchInput(String productId) {
        org.openqa.selenium.WebElement input = driver.findElement(org.openqa.selenium.By.cssSelector("input[placeholder='Search for Product Rule']"));
        input.click();
        input.clear();
        input.sendKeys(productId);
    }

    // Add more methods as needed
} 