package core.ui.actions;

import core.ui.page.PreviewPage;
import core.ui.page.PreviewTemplatePage;
import lib.enums.UnbxdEnum;
import org.fluentlenium.core.domain.FluentWebElement;
import org.testng.Assert;

import java.security.PublicKey;

public class PreviewTemplateActions extends PreviewTemplatePage {
    public void selectTemplate(UnbxdEnum type) {
        switch (type) {
            case Template_1:
                click(previewTemplate1);
                break;

            case Template_2:
                click(previewTemplate2);
                break;

            case Template_3:
                click(previewTemplate3);
                break;

            case Template_4:
                click(previewTemplate4);
                break;

            case Template_5:
                click(previewTemplate5);
                break;

            case Template_6:
                scrollToElement(previewTemplate6, templates);
                click(previewTemplate6);
                break;

            case Template_7:
                scrollToElement(previewTemplate7, templates);
                click(previewTemplate7);
                break;

            case Template_8:
                scrollToElement(previewTemplate8, templates);
                click(previewTemplate8);
                break;

            default:
        }
    }

    public void closePopUpIfOpened() {
        if (awaitForElementPresence(templatePopupCloseButton) == true) {
            click(templatePopupCloseButton);
        }
    }

    public boolean verifyFieldPresence(UnbxdEnum type) {
        boolean flag=false;
        switch (type) {
            case Preview_Title:
                int titleCount=productTitle.size();
                if(titleCount > 0) {
                    flag=true;
                    break;
                }

            case Preview_Badges:
                int badgeCount=badges.size();
                if(badgeCount > 0) {
                    flag=true;
                    break;
                }
        }
        return flag;
    }

    public String getAppliedTemplateName() {
        return appliedTemplateName.getText();
    }


}
