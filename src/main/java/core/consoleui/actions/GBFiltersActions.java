package core.consoleui.actions;

import core.consoleui.pages.GBFiltersPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class GBFiltersActions {
    private WebDriver driver;
    private GBFiltersPage page;

    public GBFiltersActions(WebDriver driver) {
        this.driver = driver;
        this.page = PageFactory.initElements(driver, GBFiltersPage.class);
    }

    // Click Minimize this button
    public void clickMinimizeThisBtn() {
        page.minimizeThisBtn.click();
    }

    // Click Add new filter set
    public void clickAddNewFilterSetBtn() {
        page.addNewFilterSetBtn.click();
    }

    // Select attribute from dropdown
    public void selectAttribute(String attribute) throws InterruptedException {
        page.attributeDropdownInput.click();
        page.attributeDropdownInput.clear();
        page.attributeDropdownInput.sendKeys(attribute);
        Thread.sleep(500); // Wait for dropdown to populate
        for (WebElement option : page.attributeDropdownOptions) {
            if (option.getText().equalsIgnoreCase(attribute)) {
                option.click();
                return;
            }
        }
        throw new RuntimeException("Attribute option not found: " + attribute);
    }

    // Select operator from dropdown
    public void selectOperator(String operator) throws InterruptedException {
        page.operatorDropdownBtn.click();
        Thread.sleep(500); // Wait for dropdown to populate
        for (WebElement option : page.operatorDropdownOptions) {
            if (option.getText().equalsIgnoreCase(operator)) {
                option.click();
                return;
            }
        }
        throw new RuntimeException("Operator option not found: " + operator);
    }

    // Select action from dropdown
    public void selectAction(String action) throws InterruptedException {
        page.actionsDropdownInput.click();
        page.actionsDropdownInput.clear();
        page.actionsDropdownInput.sendKeys(action);
        Thread.sleep(500); // Wait for dropdown to populate
        for (WebElement option : page.actionsDropdownOptions) {
            if (option.getText().equalsIgnoreCase(action)) {
                option.click();
                return;
            }
        }
        throw new RuntimeException("Action option not found: " + action);
    }

    // Click Save button
    public void clickSaveBtn() {
        page.saveBtn.click();
    }

    // Click Edit icon
    public void clickEditIcon() {
        page.editIconBtn.click();
    }

    // Click Delete icon
    public void clickDeleteIcon() {
        page.deleteIconBtn.click();
    }

    // Click YES button (confirm delete)
    public void clickYesBtn() {
        page.yesBtn.click();
    }
} 