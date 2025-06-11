package core.ui.actions;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import core.ui.page.FacetableFieldsPage;
import lib.Config;
import lib.EnvironmentConfig;
import lib.Helper;
import lib.api.ApiClient;
import lib.api.HttpMethod;
import lib.api.UnbxdResponse;
import lib.enums.UnbxdEnum;
import lombok.Data;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacetableFieldsActions extends FacetableFieldsPage {

    FeedUploadActions feedUploadActions;

    public void resetRelevancy(String sitekey,String ssoIdKeyCookie,String state)
    {
        ApiClient apiClient = new ApiClient();
        HashMap<String,String> cookies = new HashMap<>();

        String url = EnvironmentConfig.getServiceUrl("console")+"/skipper/site/"+sitekey+"/ftu/reset";

        //cookies.put(Config.getStringValueForProperty("ssoIdKey"),Config.getStringValueForProperty("relevancySsoIdKeyValue"));
        cookies.put(Config.getStringValueForProperty("ssoIdKey"),ssoIdKeyCookie);
        cookies.put(Config.getStringValueForProperty("userId"),Config.getStringValueForProperty("userIdKeyValue"));

        HashMap<String,String> header = new HashMap<>();
        header.put(Config.getStringValueForProperty("postType"),Config.getStringValueForProperty("postValue"));

        //UnbxdResponse response = apiClient.executeRequest(HttpMethod.POST,url,cookies,header,null,null,"{\"stateType\":\"SETUP_SEARCH\"}");
        UnbxdResponse response = apiClient.executeRequest(HttpMethod.POST,url,cookies,header,null,null,state);

        System.out.println(response.getResponseBody());
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Data
    static class ErrorDto
    {
        @SerializedName("@class")
        String className;
        List<Map<String, String>> errors;
    }


    public void goToRelevancyPage()
    {
        click(manualSetUpButton);
        waitForLoaderToDisAppear(feedUploadActions.pageLoader,relevancePageLoader,Config.getIntValueForProperty("indexing.numOfRetries"),Config.getIntValueForProperty("indexing.wait.time"));
    }

    public String getFacetCount()
    {
        String count= facetCount.getText();
        return count;
    }

    public void goToRelevancySectionsByName(UnbxdEnum type)
    {
        waitForLoaderToDisAppear(feedUploadActions.pageLoader,relevancePageLoader,Config.getIntValueForProperty("indexing.numOfRetries"),Config.getIntValueForProperty("indexing.wait.time"));
        threadWait();
        FluentWebElement section = getGroup(type);
        click(section);
    }

    public List<String> getFacetableFields()
    {
        List<String> listOfFeaturedFacets = new ArrayList<>();

        for(int i = 0; i < facetList.size(); i++)
        {
            listOfFeaturedFacets.add(facetList.get(i).find(".field-name").getText().trim());
            System.out.println("facets are" +listOfFeaturedFacets);
        }
        return listOfFeaturedFacets;
    }

    public void selectPageCount(String pageCount)
    {
        scrollToBottom();
        awaitForElementPresence(paginationDropDown);
        click(paginationDropDown);

        for(int i=0; i < pageCountList.size(); i++)
        {
            if(pageCountList.get(i).getText().equals(pageCount))
            {
                click(pageCountList.get(i));
                await();
                break;
            }
        }
    }

    public FluentWebElement getGroup(UnbxdEnum type)
    {
        switch(type)
        {
            case SEARCHABLE_FIELD:
                return searchableFieldGroup;
            case FACETABLE_FIELD:
                return facetableFieldGroup;
            case DICTIONARY:
                return dictionaryGroup;
            case AUTO_SUGGEST:
                return autoSuggestGroup;
            default:
                return null;
        }
    }

    public void openCreateFacetForm()
    {
       click(createNewFacetTab);
    }

    public String fillFacetDetails(Map<String, Object> testData) throws InterruptedException {
        String name = (String) testData.get("facetDisplayName");
        String facetAttribute = (String) testData.get("facetName");
        String facetVisibility = (String) testData.get("facetDisplayValue");

        awaitForElementPresence(facetAttributeDropDown);
        click(facetAttributeDropDown);
        await();
        attributeInputBox.fill().with(facetAttribute);
        Thread.sleep(3000);
        selectValueBYMatchingText(facetAttribute);
        Assert.assertTrue(attributeInput.getText().equalsIgnoreCase(facetAttribute));
        displayNameInput.fill().with(name);
        if(facetVisibility!= null && testData.get("rangeStart") != null) {
            click(rangeFacetEnableDisableDropdown);
            threadWait();
            selectValueBYMatchingText(facetVisibility);
            Assert.assertTrue(rangeFacetEnableDisableInput.getText().equalsIgnoreCase(facetVisibility));
        }
        else
        {
            click(facetEnableDisableDropdown);
            threadWait();
            selectValueBYMatchingText(facetVisibility);
            Assert.assertTrue(facetEnableDisableInput.getText().equalsIgnoreCase(facetVisibility));
        }


        if((testData.get("rangeStart") != null))
        {
            String rangeStart = (String) testData.get("rangeStart");
            String rangeEnd = (String) testData.get("rangeStop");
            String rangeGap = (String) testData.get("rangeGap");

            startRangeInput.fill().with(rangeStart);
            endRangeInput.fill().with(rangeEnd);
            rangeGapInput.fill().with(rangeGap);


        }else
        {
            String facetLength = (String) testData.get("facetLength");
            String order = (String) testData.get("sortOrder");

            facetLengthInput.fill().with(facetLength);
            sortOrderDropDown.click();
            ThreadWait();
            selectValueBYMatchingText(order);
            Assert.assertTrue(getFacetSortOrder().equalsIgnoreCase(order));
        }
        return name;

    }

    public String getFieldTypeFromEditWindow(FluentWebElement facetElement) {
        return editWindowfacetType.getValue().trim();
    }

    public String getDisplayName(FluentWebElement facetElement)
    {
        await();
        return facetElement.find(displayName).getText().trim();
    }

    public String getAttributeName(FluentWebElement facetElement)
    {
        await();
        return facetElement.find(attributeName).getText().trim();
    }

    public String getFacetStatus(FluentWebElement facetElement)
    {
        await();
        return facetElement.find(facetState).getText().trim();
    }

    public String getFieldType(FluentWebElement facetElement)
    {
        await();
        return facetElement.find(facetType).getText().trim();

    }

    public void updateFacetLength(String length)
    {
        awaitForElementPresence(facetLengthInput);
        facetLengthInput.fill().with(length);
    }

    public String getFacetLength()
    {
        awaitForElementPresence(facetLengthInput);
        return facetLengthInput.getValue().trim();
    }

    public void updateFacetSortOrder(String order)
    {
        awaitForElementPresence(sortOrderDropDown);
        sortOrderDropDown.click();
        selectValueBYMatchingText(order);
    }

    public String getFacetSortOrder()
    {
        awaitForElementPresence(sortOrderInput);
        return sortOrderInput.getText().trim();
    }

    public void updateFacetState(String state)
    {
        awaitForElementPresence(facetEnableDisableDropdown);
        facetEnableDisableDropdown.click();
        selectValueBYMatchingText(state);
    }

    public void saveFacet()
    {
        awaitForElementPresence(saveFacetButton);
        click(saveFacetButton);
        threadWait();
    }

    public FluentWebElement getFacetUsingDisplayName(String name)
    {
        awaitForElementPresence(facetSearchBox);
        ThreadWait();
        facetSearchBox.clear();
        ThreadWait();
        facetSearchBox.fill().with(name);
        ThreadWait();
        for(int i=0 ; i<facetList.size() ; i++)
        {
            if(facetList.get(i).find(".field-name:nth-child(3)").getText().trim().equals(name))
            return facetList.get(i);
        }
        return null;
    }


    public void closeFacetCreationWindow()
    {
        awaitForElementPresence(facetWindowCloseButton);
        click(facetWindowCloseButton);
    }

    public void clearSearchBox()
    {
        awaitForElementPresence(facetSearchBox);
        facetSearchBox.clear();
    }

    public void deleteFacet(String name,String pageCount) throws InterruptedException
    {
        FluentWebElement facet = getFacetUsingDisplayName(name);
        Helper.mouseOver(facet.getElement());
        Thread.sleep(10000);
        click(deleteFacetIcon);
        awaitForElementPresence(deleteWindow);
        Assert.assertTrue(deleteWindow.isDisplayed(),"Facet Edit Window is not opened");
        awaitForElementPresence(deleteConfirmationTab);
        click(deleteConfirmationTab);
        threadWait();
        awaitForElementNotDisplayed(deleteWindow);
    }

    public String createFacet() throws InterruptedException {
        Map<String, Object> testData = new HashMap<>();
        //testData.put("facetDisplayName", getRandomName());
        testData.put("facetDisplayName", "brand"+System.currentTimeMillis());
        //testData.put("facetName", "gender");
        testData.put("facetName", "brand");
        testData.put("facetLength", "4");
        testData.put("sortOrder", "Product Count");
        testData.put("facetDisplayValue","Enabled");

        String name = fillFacetDetails(testData);
        saveFacet();

        return name;
    }

    public FluentWebElement getFacetByPosition(int position)
    {
        if(facetList.size()>0)
        {
            for(int i=0 ; i<facetList.size();i++)
            {
                return facetList.get(position);
            }
        }
        return null;
    }

    public void selectFacetAttributeFromDropdown(String attributeName)
    {
        click(facetAttributeDropDown);
        attributeInputBox.fill().with(attributeName);

        for(int i=0 ; i<attributeList.size() ; i++)
        {
            if(attributeList.get(i).getText().equalsIgnoreCase(attributeName))
            {
                click(attributeList.get(i));
                break;
            }
        }
    }

    public void disableFacet(String name)
    {
        getFacetUsingDisplayName(name);

    }

    public void openFacetEditWindow(FluentWebElement facet) throws InterruptedException
    {
        ThreadWait();
        Helper.mouseOver(facet.getElement());
        Thread.sleep(10000);
        click(editFacetIcon);
        awaitForElementPresence(editWindow);
        Assert.assertTrue(editWindow.isDisplayed(),"Facet Edit Window is not opened");
    }

    public void skipQueryData()
    {
        awaitForElementPresence(skipQueryDataButton);
        click(skipQueryDataButton);
    }
}
