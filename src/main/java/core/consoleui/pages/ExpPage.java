package core.consoleui.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExpPage {
    @FindBy(xpath = "//div[@class=\"modal-content\"]//div[@class=\"modal-header\"]//button")
    public WebElement popupCloseBtn;

    @FindBy(id = "pushActionRefuse")
    public WebElement pushActionRefuseBtn;

    @FindBy(css = "button.app-common-button.btn.btn-primary")
    public WebElement createExperienceBtn;

    @FindBy(css = "div.modifier.align-self-center > input[type='text']")
    public WebElement experienceNameInput;

    @FindBy(css = "div[class='dropdown'] div[class='dropdown-label-text d-inline-block']")
    public WebElement pageDropdown;

    // Option for pageType will be found dynamically

    @FindBy(xpath = "//div[@class='rex-form unbxd-form']//div[1]//div[2]//div[2]//div[1]//button[1]")
    public WebElement widgetDropdown;

    // Option for widgetType will be found dynamically

    @FindBy(css = "#multiselectContainerReact .searchBox")
    public WebElement templateSearchInput;

    @FindBy(css = "#multiselectContainerReact .option")
    public java.util.List<WebElement> templateOptions;

    @FindBy(css = "div[class='col-12 p-0 m-0 algo-dropdown d-flex align-items-center'] div[class='dropdown-label-text d-inline-block']")
    public WebElement algoDropdown;

    // Option for customAlgo will be found dynamically

    @FindBy(css = ".input-value.input-group input[placeholder='Display Name']")
    public WebElement displayNameInput;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    public WebElement saveBtn;

    @FindBy(css = "div.list-crud-items .icon-edit")
    public WebElement editExpButton;

    @FindBy(css = "div.list-crud-items .icon-delete")
    public WebElement deleteExpButton;

    @FindBy(css = "button[class='btn btn-primary']")
    public WebElement confirmDeleteBtn;

    @FindBy(css = "input[placeholder='Search for Experience']")
    public WebElement searchExpInput;

    @FindBy(xpath = "//span[text()='Advanced filter']")
    public WebElement advancedFilterBtn;

    @FindBy(css = "#dropdown-basic")
    public WebElement advancedFilterDropdown;

    @FindBy(css = "div.dropdown-menu.show[aria-labelledby='dropdown-basic'] a.dropdown-item")
    public java.util.List<WebElement> advancedFilterDropdownOptions;

    @FindBy(css = "#dropdown-basic")
    public WebElement selectDropdownBtn;

    @FindBy(css = "div.dropdown-menu.show[aria-labelledby='dropdown-basic'] a.dropdown-item")
    public java.util.List<WebElement> selectDropdownOptions;

    @FindBy(css = ".icon-add")
    public WebElement addIconBtn;

    @FindBy(css = ".reset-and-remove")
    public WebElement resetAndRemoveBtn;

    @FindBy(css = ".advanced-filter-link")
    public WebElement advancedFilterLink;
} 