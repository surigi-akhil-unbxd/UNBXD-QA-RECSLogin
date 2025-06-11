package core.ui.page;

import core.ui.actions.PreviewActions;
import lib.annotation.FileToTest;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

public class PreviewPage extends UiBase {

    @FindBy(css=".RCB-btn-small")
    public FluentWebElement previewButtonTab;

    @FindBy(css= ".RCB-btn-large")
    public FluentWebElement applyConfigButton;

    @FindBy(css=".UNX-product-list .UNX-parent-image")
    public FluentList<FluentWebElement> productSnippet;

    @FindBy(css=".UNX-facet-item-d")
    public FluentList<FluentWebElement> filterSnippet;

    @FindBy(css=".UNX-result-info")
    public FluentWebElement productCount;

    @FindBy(css=".RCB-modal-close")
    public FluentWebElement templatePopupCloseButton;

    @FindBy(xpath="//*[@id='unbxdInput']")
    public FluentWebElement searchInputBox;

    //@FindBy(xpath="//div/ul/li[@data-type='KEYWORD_SUGGESTION']")
    @FindBy(css=".unbxd-as-sorted-suggestion")
    public FluentList<FluentWebElement> KeywordSuggestionList;

   // @FindBy(xpath="//*[contains(@class,'unbxd-as-insuggestion')]")
    @FindBy(css=".unbxd-as-insuggestion")
    public FluentList<FluentWebElement> suggestInfield;

   // @FindBy(xpath="//li/div//img")
    @FindBy(css=".unbxd-as-popular-product-image-container")
    public FluentList<FluentWebElement> popularProduct;

    @FindBy(id="searchBtn")
    public FluentWebElement searchIcon;

    // @FindBy(xpath="(//*[@class='UNX-suggestion-p']//following::*)[1]")
    @FindBy(css=".UNX-suggestion-p strong")
    public FluentWebElement searchedQueryName;

    @FindBy(id="didYouMeanWrapper")
    public FluentWebElement didYouMeanLink;

    @FindBy(css=".unbxd-as-maincontent")
    public FluentWebElement keywordSuggestionPresence;

    @FindBy(css=".unbxd-as-sidecontent")
    public FluentWebElement popularProductPresence;

    @FindBy(css=".UNX-selected-facet-header")
    public FluentWebElement selectedFilterHeader;

    @FindBy(css=".UNX-facet-text")
    public FluentList<FluentWebElement> FilterFacet;

    @FindBy(css=".UNX-product-title")
    public FluentList<FluentWebElement> ProductTitles;


}
