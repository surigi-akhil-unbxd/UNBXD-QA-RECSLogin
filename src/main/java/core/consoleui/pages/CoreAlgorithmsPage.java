package core.consoleui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CoreAlgorithmsPage {
    private WebDriver driver;

    // Example WebElement definitions (update selectors as needed)
    @FindBy(css = "button.create-new")
    public WebElement createNewButton;

    @FindBy(css = "input[type='text'][maxlength='70']")
    public WebElement algorithmNameInput;

    

    @FindBy(css="li[class='sub-menu-list core-selected custom'] div[class='side-innermenu-title']")
    public WebElement coreAlgorithms;

    @FindBy(css = "div.minimize-title")
    public WebElement minimizeTitle;

    @FindBy(xpath = "//tbody/tr[2]/td[2]/div[1]/button[1]")
    public WebElement CTLconfigureButton;

    @FindBy(css = "button:nth-child(2)")
    public WebElement createButton;

    @FindBy(css = ".source-product input.form-control.form-control-small[type='text']")
    public WebElement enterProductIdInput;

    @FindBy(xpath = "//div[@class='mapped-categories']//input[1]")
    public WebElement enterChildIdInput;

    @FindBy(css = "div.page-save-controller button[type='button']")
    public WebElement saveButton;

    @FindBy(css = ".list-view-footer .icon.icon-edit")
    public WebElement editIcon;

    @FindBy(css = ".list-view-footer .icon.icon-delete")
    public WebElement deleteIcon;

    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and contains(text(), 'Proceed')]")
    public WebElement proceedButton;


    @FindBy(css=".icon-add")
    public WebElement addButton;

   @FindBy(css = "button[class='btn btn-primary']")
    public WebElement ProceedButton;

    @FindBy(css = "button[class='btn btn-primary']")
    public WebElement yesBtn;

    // Add more elements as needed

    public CoreAlgorithmsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
} 