package core.consoleui.actions;

import core.consoleui.pages.CustomAlgorithmsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import lib.EnvironmentConfig;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public void searchCustomAlgorithmByName(String name) {
        page.searchCustomAlgorithmInput.click();
        page.searchCustomAlgorithmInput.clear();
        page.searchCustomAlgorithmInput.sendKeys(name);
    }

    public boolean isAlgorithmPresentInListing(String algoName) {
        for (org.openqa.selenium.WebElement el : page.algorithmNameList) {
            if (el.getText().trim().equalsIgnoreCase(algoName.trim())) {
                return true;
            }
        }
        return false;
    }

    public String enterRandomCursorAlgoName() {
        page.enterNameField.click();
        // Use Command+A (Mac) and Delete to clear
        page.enterNameField.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.COMMAND, "a"));
        page.enterNameField.sendKeys(org.openqa.selenium.Keys.DELETE);
        CustomAlgoName = "cursorAlgo" + System.currentTimeMillis();
        page.enterNameField.sendKeys(CustomAlgoName);
        return CustomAlgoName;
    }

    public void clickEditButton() {
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
}