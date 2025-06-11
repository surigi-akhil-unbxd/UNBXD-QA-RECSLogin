package core.consoleui.pages;

import core.ui.page.UiBase;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import lib.EnvironmentConfig;
import java.util.List;

public class GBFiltersPage extends UiBase {

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

    @FindBy(id = "pushActionRefuse")
    public FluentWebElement noThanksButton;

    @FindBy(css = ".popup-close")
    public FluentWebElement popupClose;

    @FindBy(css = "li[id='rbt-menu-item-0'] a[class='dropdown-item']")
    public FluentWebElement firstActionOption;

    @FindBy(css = ".filter-sets-list")
    public FluentWebElement filterSetsList;

    @FindBy(css = ".filter-set-item")
    public FluentWebElement filterSetItem;

    @FindBy(css = "div[class='filter-group-footer'] div:nth-child(2) span:nth-child(2)")
    public FluentWebElement editButton;

    @FindBy(css = "div[class='filter-group-footer'] div:nth-child(1) span:nth-child(2)")
    public FluentWebElement deleteButton;

    @FindBy(css = "button[class='btn btn-primary']")
    public FluentWebElement confirmDeleteButton;

    @FindBy(css = ".filter-set-form")
    public FluentWebElement filterSetForm;

    @FindBy(css = "div.minimize-title")
    public FluentWebElement minimizeTitle;

    @FindBy(css = "div.minimize-title")
    public WebElement minimizeThisBtn;

    @FindBy(css = "div.add-new-filter")
    public WebElement addNewFilterSetBtn;

    @FindBy(css = "input.rbt-input-main.form-control.rbt-input.input-sm.form-control-sm")
    public WebElement attributeDropdownInput;

    @FindBy(css = "div.dropdown-menu.show .dropdown-item")
    public List<WebElement> attributeDropdownOptions;

    @FindBy(css = "button#dropdown-basic.default-text.dropdown-toggle.btn.btn-success")
    public WebElement operatorDropdownBtn;

    @FindBy(css = "div.dropdown-menu.show .dropdown-item")
    public List<WebElement> operatorDropdownOptions;

    @FindBy(css = "div.rbt-input-wrapper input.rbt-input-main")
    public WebElement actionsDropdownInput;

    @FindBy(css = "div.dropdown-menu.show .dropdown-item")
    public List<WebElement> actionsDropdownOptions;

    @FindBy(css = "button.btn.btn-primary")
    public WebElement saveBtn;

    @FindBy(css = "span.icon.icon-edit")
    public WebElement editIconBtn;

    @FindBy(css = "span.icon-text")
    public WebElement deleteIconBtn;

    @FindBy(xpath = "//button[@type='button' and contains(@class,'btn-primary') and normalize-space()='YES']")
    public WebElement yesBtn;

    @Override
    public String getUrl() {
        // Use UrlMapper or construct the URL as per your environment
        return EnvironmentConfig.getBaseUrl() + "/recs-v2/sites/" + EnvironmentConfig.getSiteId() + "/manage/global-filters";
    }

    @Override
    public void isAt() {
        awaitForElementPresence(algorithmMenu);
    }
} 