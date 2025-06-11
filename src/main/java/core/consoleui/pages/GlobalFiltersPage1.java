package core.consoleui.pages;

import core.ui.page.UiBase;
import lib.EnvironmentConfig;
import org.openqa.selenium.WebElement;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.google.common.base.Function;
import org.fluentlenium.core.Fluent;

public class GlobalFiltersPage1 extends UiBase {

    @FindBy(css = ".side-menu-list.algo-selected")
    public FluentWebElement algorithmMenu;

    @FindBy(css = ".sub-menu-list.global-selected")
    public FluentWebElement globalFilterMenu;

    @FindBy(css = ".add-new-filter")
    public FluentWebElement addNewFilterButton;

    @FindBy(css = ".filterRuleHeader")
    public FluentWebElement filterRulesHeader;

    @FindBy(css = "input[role='combobox']")
    public FluentWebElement attributeInput;

    @FindBy(css = "#dropdown-basic")
    public FluentWebElement operatorDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Include')]")
    public FluentWebElement includeOperator;

    @FindBy(css = ".filter-typeahead-input .rbt-input-main")
    public FluentWebElement actionsInput;

    @FindBy(css = ".btn-primary")
    public FluentWebElement saveButton;

    @FindBy(css = ".message")
    public FluentWebElement successMessage;

    @FindBy(css = ".popup-close")
    public FluentWebElement popupClose;



      @FindBy(id = "pushActionRefuse")
  public FluentWebElement noThanksButton;

    @Override
    public String getUrl() {
        return EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/1299/manage/global-filters";
    }

    @Override
    public void isAt() {
        awaitForElementPresence(algorithmMenu);
    }

    @Override
    public Boolean awaitForElementPresence(final FluentWebElement element) {
        Function<Fluent, FluentWebElement> function = new Function<Fluent, FluentWebElement>() {
            public FluentWebElement apply(Fluent fluent) {
                if (element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        };
        try {
            await().atMost(getMaxTimeout()).until(function);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean awaitForElementNotDisplayed(final FluentWebElement element) {
        Function<Fluent, FluentWebElement> function = new Function<Fluent, FluentWebElement>() {
            public FluentWebElement apply(Fluent fluent) {
                if (!element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        };
        try {
            await().atMost(getMaxTimeout()).until(function);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
} 