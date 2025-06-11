package core.ui.actions;

import core.ui.page.AutoMappingPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoMappingActions extends AutoMappingPage {


    public String getMappedFieldUsingDisplayName(String field) {
       // awaitForElementPresence(setUpSearchButton);
        for (int i = 0; i < mapList.size(); i++) {
            if (mapList.get(i).find(".flex-one .dimension-label").getText().trim().equals(field)) {
                return mapList.get(i).find(".RCB-inline-modal-btn .RCB-dd-label").getText();
                //  return dimensionMapInput.getText();
            }
        }
        return null;
    }


    public void mapFields() throws InterruptedException {
        Map<String, Object> testData = new HashMap<>();
        testData.put("title", "product_name");
        testData.put("Image URL", "product_image_url");
        testData.put("Product URL", "product_image_url");
        testData.put("category", "brand");

         fillMapDetails(testData);
    }

    public void fillMapDetails(Map<String, Object> testData) throws InterruptedException
    {
        for (Map.Entry a : testData.entrySet())
        {
            FluentWebElement field = findMappingField((String)a.getKey());
            selectMappingValue(field,(String) a.getKey(),(String) a.getValue());
        }
    }

    public FluentWebElement findMappingField(String mapField)
    {
        for (int i = 0; i < mapList.size(); i++)
        {
            if (mapList.get(i).find(".flex-one .dimension-label").getText().trim().equalsIgnoreCase(mapField))
                return mapList.get(i);
        }
        return null;
    }

    public void selectMappingValue(FluentWebElement field, String mapField,String mapValue) throws InterruptedException
    {
        await();
        scrollToElement(field,mapField);
        Thread.sleep(3000);
        field.find(".RCB-select-arrow").click();
        mapSearch.fill().with(mapValue);
        Thread.sleep(3000);
        selectDropDownValue(mapDropDownList,mapValue);
        Thread.sleep(3000);
        //Assert.assertEquals(getMappedFieldUsingDisplayName(mapField),mapValue);
    }

    public void saveMappingChanges()
    {
        awaitForElementPresence(saveButton);
        click(saveButton);
    }
}



