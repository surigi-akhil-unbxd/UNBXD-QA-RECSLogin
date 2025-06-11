package core.ui.page;

import lib.enums.UnbxdEnum;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UnbxdCommonPage extends UiBase {


    @FindBy(xpath = "//ul[contains(@class,'unx-site-sidebar-menus')]/li[contains(@class,'unx-segment-menu')]/a")
    public FluentWebElement segments;

    @FindBy(xpath = "//ul[contains(@class,'unx-site-sidebar-menus')]/li[contains(@class,'unx-merchandise-menu')]")
    public FluentWebElement merchandising;


    @FindBy(xpath = "//li[contains(@class,'unx-product-widget')/div[contains(@class,'unx-switch-prod')]]")
    public FluentWebElement switchProducts;



    @FindBy(xpath = "//ul[contains(@class,'unx-site-sidebar-menus')]/li[contains(@class,'unx-manage-menu')]")
    public FluentWebElement manageConfiguration;

    @FindBy(xpath="//ul[contains(@class,'unx-site-sidebar-menus')]/li[contains(@class,'unx-reports-menu')]")
    public FluentWebElement reports;

    @FindBy(xpath = "//div[contains(@class,'unx-campaign-footer')]//ul//li[contains(@class,'unx-footer-item')]//a[contains(@class,'unx-saved')]")
    public FluentWebElement savedLabel;

    @FindBy(xpath = "//div[contains(@class,'unx-campaign-footer')]//ul//li[contains(@class,'unx-footer-item')]//a[contains(@class,'unx-saving')]")
    public FluentWebElement savingLabel;

    @FindBy(xpath = "//li//span[contains(text(),'PUBLISH')]")
    public FluentWebElement publishButton;

    @FindBy(xpath = "//li//span[contains(text(),'RE-PUBLISH')]")
    public FluentWebElement republishButton;

    @FindBy(xpath = "//li/a//span[contains(text(), 'NEXT')]")
    public FluentWebElement nextButton;

    @FindBy(xpath = "//li/a//span[contains(text(), 'PREV')]")
    public FluentWebElement previousButton;


    @FindBy(xpath = "//a[contains(@class,'unx-search-box')]//input[contains(@class,'unx-input-search')]")
    public FluentWebElement searchBox;

    @FindBy(xpath = "//a[contains(@class,'unx-search-box')]//i[contains(@class,'unx-open-search')]")
    public FluentWebElement searchBoxOpen;

    @FindBy(xpath = "//a[contains(@class,'unx-search-box')]//i[contains(@class,'unx-close-search')]")
    public FluentWebElement  searchClose;


    public void goTo(UnbxdEnum section)
    {

        FluentWebElement sectionElement=null;
        if(!merchandising.getAttribute("class").contains("nav-active"))
            click(merchandising.findFirst("a i.glyphicon-menu-down"));
        getModuleByName(section).click();
        awaitForPageToLoad();
    }

    public void goToConfiguration(UnbxdEnum configurationType)
    {
        manageConfiguration.findFirst("a i.glyphicon-menu-down").click();
        FluentWebElement element=manageConfiguration.findFirst("a:contains('"+configurationType.getLabel()+"')");
        element.click();
        awaitForPageToLoad();
    }


    public void switchToProduct(UnbxdEnum type)
    {

        click(switchProducts.findFirst("a"));
        getDriver().findElement(By.xpath("//ul[contains(@class,'unx-product-list')]//li//a[contains(text(),"+type.getLabel()+")]"));
        awaitForPageToLoad();
    }

    public WebElement getModuleByName(UnbxdEnum module)
    {
        String xpath="//ul[@class='sub-menu-list']//a//span[contains(text(),'"+module.getLabel()+"')]";
        return getDriver().findElement(By.xpath(xpath));

    }


    public boolean checkModalWindow()
    {
        if(findFirst("body").getAttribute("class").contains("modal-open"))
            return true;

        else
            return false;
    }







}
