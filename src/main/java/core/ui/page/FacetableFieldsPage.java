package core.ui.page;

import lib.EnvironmentConfig;
import lib.UrlMapper;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

public class FacetableFieldsPage extends RelevancyPage {

    public static By setUpSearch = By.cssSelector(".setup-search-btn");

    public String setUpSearchName = "Setup search";



    @FindBy(css = ".manual-setup-btn")
    public FluentWebElement manualSetUpButton;

    public String relevancePageLoader = "Relevancy page loader";
    public String applyButton = "Apply button";
    public static By relevancyPageLoader = By.cssSelector(".page-loader");
    public static By relevancyPageLoader1 = By.cssSelector(".mask-loader");
    public static By facetSection = By.cssSelector(".RCB-tab-title:nth-child(2)");

    @FindBy(css = ".RCB-tab-title:nth-child(2)")
    public FluentWebElement facetableFieldGroup;

    @FindBy(css = ".RCB-tab-title:nth-child(1)")
    public FluentWebElement searchableFieldGroup;

    @FindBy(css = ".RCB-tab-title:nth-child(3)")
    public FluentWebElement dictionaryGroup;

    @FindBy(css=".RCB-tab-title:nth-child(4)")
    public FluentWebElement autoSuggestGroup;

    @FindBy(css=".RCB-modal-close")
    public FluentWebElement facetWindowCloseButton;

    @FindBy(css = ".pill-container b")
    public FluentWebElement facetCount;

    @FindBy(css=".facet-list-container .RCB-list .facet-list-body")
    public FluentList<FluentWebElement> facetList;

    @FindBy(css = ".RCB-select-arrow")
    public FluentWebElement paginationDropDown;

    @FindBy(css = ".RCB-inline-modal-body .RCB-list li")
    public FluentList<FluentWebElement> pageCountList;

    @FindBy(css=".add-new-button")
    public FluentWebElement createNewFacetTab;

    @FindBy(name="display_name")
    public FluentWebElement displayNameInput;

    @FindBy(name="facet_length")
    public FluentWebElement facetLengthInput;

    @FindBy(name="facet_type")
    public FluentWebElement editWindowfacetType;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(7) .RCB-select-arrow")
    public FluentWebElement rangeFacetEnableDisableDropdown;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(6) .RCB-select-arrow")
    public FluentWebElement facetEnableDisableDropdown;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(1) .RCB-select-arrow")
    public FluentWebElement facetAttributeDropDown;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(1) .RCB-inline-modal-btn")
    public FluentWebElement attributeInput;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(6) .RCB-inline-modal-btn")
    public FluentWebElement facetEnableDisableInput;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(7) .RCB-inline-modal-btn")
    public FluentWebElement rangeFacetEnableDisableInput;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(3) .RCB-select-arrow")
    public FluentWebElement sortOrderDropDown;

    @FindBy(css=".new-facet-form .RCB-dropdown:nth-child(3) .RCB-inline-modal-btn span")
    public FluentWebElement sortOrderInput;


    @FindBy(css=".RCB-dd-search-ip")
    public FluentWebElement attributeInputBox;

    @FindBy(css=".RCB-align-left .RCB-list-item")
    public FluentList<FluentWebElement> attributeList;

    @FindBy(name="range_start")
    public FluentWebElement startRangeInput;

    @FindBy(name = "range_end")
    public FluentWebElement endRangeInput;

    @FindBy(name = "range_gap")
    public FluentWebElement rangeGapInput;

    @FindBy(css=".new-facet-container .RCB-btn")
    public FluentWebElement saveFacetButton;

    @FindBy(css=".search-input-box .RCB-form-el")
    public FluentWebElement facetSearchBox;

    @FindBy(css=".delete-icon")
    public FluentWebElement deleteFacetIcon;

    @FindBy(css=".modal-actions .RCB-btn-primary")
    public FluentWebElement deleteConfirmationTab;

    @FindBy(css=".facet-notifier-container .RCB-notif-success")
    public FluentWebElement facetSuccessMessage;

    @FindBy(css=".facet-row-edit .edit-icon")
    public FluentWebElement editFacetIcon;

    @FindBy(css=".new-facet-container .RCB-modal-body")
    public FluentWebElement editWindow;

    @FindBy(css=".delete-facet-container .RCB-modal-body")
    public FluentWebElement deleteWindow;

    @FindBy(css=".skip-step")
    public FluentWebElement skipQueryDataButton;

    public String facetState =".facet-status";
    public String attributeName = ".field-name:nth-child(2)";
    public String displayName = ".field-name:nth-child(3)";
    public String facetType = ".list-item:nth-child(4)";







}












