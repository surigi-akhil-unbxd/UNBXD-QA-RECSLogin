package core.consoleui.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class CustomAlgorithmsPage {
   
    @FindBy(css = "li.sub-menu-list.core-selected.custom div.side-innermenu-title")
    public WebElement coreAlgorithmsTab;
    
    @FindBy(css = "li.sub-menu-list.custom-selected.custom div.side-innermenu-title")
    public WebElement customAlgorithmsTab;

    @FindBy(css=".minimize-title")
    public WebElement minimizeTitle;

    @FindBy(css=".algorithm-selector .dropdown-wrapper .dropdown > button.dropdown-toggle")
    public WebElement algorithmSelectorDropdownToggle;

    @FindBy(css=".simple-algo-slot .dropdown > button.dropdown-toggle")
    public WebElement noOfSlotsDropdown;

    @FindBy(css=".dropdown-wrapper .float-right button.vanilla-button")
    public WebElement createHybridButton;

    @FindBy(css=".filter-btn button.vanilla-button")
    public WebElement filterBtnButton;

    @FindBy(css=".fallbacks-panel .app-common-switch")
    public WebElement fallbackSwitch;

    @FindBy(css = "div.page-save-controller button[type='button']")
    public WebElement saveButton;

    @FindBy(css = "div.page-redirect-element.d-flex.align-content-center span.text")
    public WebElement backToAlgorithmListing;

    @FindBy(css = "input[type='text'][maxlength='70']")
    public WebElement enterNameField;

    @FindBy(css = "button.app-common-button.btn.btn-primary")
    public WebElement createNewButton;

    @FindBy(css = "input[placeholder='Search for Custom Algorithm']")
    public WebElement searchCustomAlgorithmInput;

    @FindBy(css = ".algorithm-listing .algorithm-name")
    public List<WebElement> algorithmNameList;

    @FindBy(css = ".list-crud-items .icon-edit")
    public WebElement editButton;

    @FindBy(css = ".list-crud-items .icon-delete")
    public WebElement deleteButton;

    @FindBy(css = "button[class='btn btn-primary']")
    public WebElement ProceedButton;

    @FindBy(css = "input[type='number'].form-control.form-control-small")
    public WebElement lookBackPeriodDropdown;

}
