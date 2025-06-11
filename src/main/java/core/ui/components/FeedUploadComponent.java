package core.ui.components;

import core.ui.actions.*;
import core.ui.page.UiBase;
import lib.Config;
import lib.UrlMapper;
import org.fluentlenium.core.annotation.Page;
import org.testng.Assert;

import java.util.regex.Pattern;

import static lib.enums.UnbxdEnum.MAGENTO_TAB;
import static lib.enums.UnbxdEnum.PLATFORM_UPLOAD;

public class FeedUploadComponent extends UiBase {

    @Page
    FeedUploadActions feedUploadActions;

    @Page
    CreateSiteActions createSiteActions;

    @Page
    CreateSiteComponent createSiteComponent;

    @Page
    AutoMappingActions autoMappingActions;

    @Page
    FacetableFieldsActions facetableFieldsActions;

    @Page
    AutoSuggestActions autoSuggestActions;

    @Page
    SearchableFieldActions searchableFieldActions;

    @Page
    PreviewActions previewActions;

    public String goTillMappingPage() throws InterruptedException {

        //CreateSite
        createSiteComponent.createSite();
        Assert.assertTrue(createSiteActions.awaitForElementPresence(createSiteActions.catalogUploadPageTitle));
        Assert.assertEquals(createSiteActions.uploadPageText.getText(), "How would you like to upload your catalog today?");

        //Upload feed

        feedUploadActions.selectUploaders(PLATFORM_UPLOAD);
        feedUploadActions.selectPlatform(MAGENTO_TAB);
        String siteKey = createSiteActions.getCreatedSiteKey();
        String secreteKey = createSiteActions.getSecreteKey();

        feedUploadActions.apiUpload(siteKey, secreteKey);
        feedUploadActions.clickOnProceedButton();
        feedUploadActions.waitForFeedUploadToComplete();
        Assert.assertTrue(feedUploadActions.successMsg.getText().equalsIgnoreCase("Feed Upload is Successful"));

        //goto Mapping page
        click(autoMappingActions.mapCatalogButton);
        // Thread.sleep(50000);
        facetableFieldsActions.waitForLoaderToDisAppear(facetableFieldsActions.relevancyPageLoader1, facetableFieldsActions.relevancePageLoader);
        facetableFieldsActions.waitForElementAppear(autoMappingActions.setUpSearchButtons, autoMappingActions.mappingPageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        facetableFieldsActions.waitForElementAppear(autoMappingActions.mapList1, autoMappingActions.mappingPageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        Assert.assertEquals(autoMappingActions.setUpSearchButton.getText(), "Set up Search");

        //Map fields
        Thread.sleep(50000);
        autoMappingActions.mapFields();
        click(autoMappingActions.setUpSearchButton);
        return siteKey;
    }

    public void goTillRelevancyPage() throws InterruptedException {

        //Go to Relevancy page
        facetableFieldsActions.waitForElementAppear(autoSuggestActions.skipQueryButton, facetableFieldsActions.relevancePageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        Thread.sleep(50000);
        Assert.assertEquals(autoSuggestActions.skipQuery.getText(), "Skip this step");
        facetableFieldsActions.skipQueryData();
        facetableFieldsActions.waitForLoaderToDisAppear(facetableFieldsActions.relevancyPageLoader, facetableFieldsActions.relevancePageLoader);
        facetableFieldsActions.waitForElementAppear(searchableFieldActions.applyAiRecButton, facetableFieldsActions.relevancePageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        facetableFieldsActions.waitForElementToBeClickable(searchableFieldActions.applyAiRec, facetableFieldsActions.applyButton);
        facetableFieldsActions.waitForLoaderToDisAppear(facetableFieldsActions.relevancyPageLoader, facetableFieldsActions.relevancePageLoader);
        facetableFieldsActions.waitForElementAppear(searchableFieldActions.applyAiRecButton, facetableFieldsActions.relevancePageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        Assert.assertEquals(searchableFieldActions.applyAiRec.getText(), "Apply configurations");
    }

    public void goTillPreviewPage() throws InterruptedException {
        facetableFieldsActions.waitForLoaderToDisAppear(facetableFieldsActions.relevancyPageLoader, facetableFieldsActions.relevancePageLoader);
        facetableFieldsActions.waitForElementAppear(searchableFieldActions.applyAiRecButton, facetableFieldsActions.relevancePageLoader, Config.getIntValueForProperty("indexing.numOfRetries"), Config.getIntValueForProperty("indexing.wait.time"));
        facetableFieldsActions.waitForElementToBeClickable(searchableFieldActions.applyAiRec, facetableFieldsActions.applyButton);
        previewActions.awaitForElementPresence(previewActions.applyConfigButton);
        Thread.sleep(50000);
        click(previewActions.applyConfigButton);
        feedUploadActions.WaitTillReindexComplete();
        await();
        previewActions.goToPreviewPage();
        previewActions.awaitForElementPresence(previewActions.productCount);
        await();
    }

    public String getStatusByIdFromUrl() {
        String currentUrl = getDriver().getCurrentUrl();

        String[] urlParts = currentUrl.split(Pattern.quote("/"));
        String statusId = urlParts[5];
        return statusId;
    }


    public String getRelevancyPage() {
        awaitForPageToLoad();
        return UrlMapper.RELEVANCY_PAGE.getBaseUrl(getStatusByIdFromUrl());
    }


}
