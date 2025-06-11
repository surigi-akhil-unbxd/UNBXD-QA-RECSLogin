package core.consoleui.actions;

import org.openqa.selenium.Keys;

import core.consoleui.pages.GlobalFiltersPage1;


public class GlobalFiltersActions1 extends GlobalFiltersPage1 {

    public void navigateToGlobalFilters() {
        awaitForElementPresence(algorithmMenu);
        algorithmMenu.click();
        globalFilterMenu.click();
        
        // Handle popup if present
        try {
            if (noThanksButton.isDisplayed()) {
                noThanksButton.click();
            }
            if (popupClose.isDisplayed()) {
                popupClose.click();
            }
        } catch (Exception e) {
            // Popup might not be present, continue
        }
    }

    public void createNewFilterSet() throws InterruptedException {
        awaitForElementPresence(addNewFilterButton);
        addNewFilterButton.click();
        awaitForElementPresence(filterRulesHeader);

        // Select attribute
        awaitForElementPresence(attributeInput);
        attributeInput.click();
        attributeInput.fill().with("uniqueId");
        awaitForElementPresence(attributeInput); // Wait for dropdown to appear
        attributeInput.getElement().sendKeys(Keys.ENTER);

        // Select operator
        awaitForElementPresence(operatorDropdown);
        operatorDropdown.click();
        awaitForElementPresence(includeOperator);
        includeOperator.click();

        // Select action
        awaitForElementPresence(actionsInput);
        actionsInput.click();
        actionsInput.fill().with("AAM15MB01");
        awaitForElementPresence(actionsInput); // Wait for dropdown to appear
        actionsInput.getElement().sendKeys(Keys.ENTER);

        // Save filter and wait for success
        awaitForElementPresence(saveButton);
        saveButton.click();
        
        // Wait for success message
        awaitForElementPresence(successMessage);
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return awaitForElementPresence(successMessage) && successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean awaitForPageToLoad() {
        return awaitForElementPresence(algorithmMenu);
    }

    public void closeNoThanksPopupIfPresent() {
        try {
            awaitForElementPresence(noThanksButton);
            if (noThanksButton.isDisplayed()) {
                noThanksButton.click();
            }
        } catch (Exception e) {
            // Popup not present, continue
        }
    }
} 