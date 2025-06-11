package core.ui.page;

import com.github.javafaker.Faker;
import com.google.common.base.Function;
import lib.BrowserInitializer;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.fluentlenium.core.Fluent;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.action.FluentDefaultActions;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static lib.constants.UnbxdConstants.SELENIUM_MAXTIMEOUT;
import static lib.constants.UnbxdConstants.SELENIUM_MINTIMEOUT;


public class UiBase extends FluentPage {

    public static Logger APPLICATION_LOGS = Logger.getLogger("Manasa");

    private static final String CONFIG_FILE = "config.properties";

    public UiBase() {
        super();
    }

    public UiBase(WebDriver driver) {
        super();
        initFluent(driver);
        initTest();
    }

   // @FindBy(css = "div.alert.alert-success")
    @FindBy(css = ".notification-outer-wrapper .unx-qa-toastsucess")
    public FluentWebElement successMessage;

    @FindBy(css = ".notification-outer-wrapper .unx-qa-toasterror")
    public FluentWebElement alertErrorMessage;

    @FindBy(css = ".RCB-align-left .RCB-list-item.selected")
    public FluentList<FluentWebElement> highlightedSearchResults;

    @FindBy(css = ".RCB-notif-error")
    public FluentWebElement alertMessage;

    @FindBy(css=".RCB-align-left .RCB-list-item")
    public FluentList<FluentWebElement> facetDropDownlist;


    public boolean checkSuccessMessage() {
        awaitForElementPresence(successMessage);
        return successMessage.isDisplayed();
    }

    public boolean checkAlertErrorMessage() {
        if (!awaitForElementPresence(alertErrorMessage))
            return false;
        return alertErrorMessage.isDisplayed();
    }

    public String getSsoIdCookie()
    {
        awaitForPageToLoad();
        getDriver().manage().getCookies();
        return getDriver().manage().getCookieNamed("_un_sso_uid").getValue();
    }

    public String getCsrfCookie()
    {
        awaitForPageToLoad();
        getDriver().manage().getCookies();
        return getDriver().manage().getCookieNamed("_un_csrf").getValue();
    }

    private static PropertiesConfiguration getPropertiesConfiguration() {
        PropertiesConfiguration config = null;
        try {
            config = new PropertiesConfiguration(loadAndGetResourceLocation(CONFIG_FILE));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static int getMaxTimeout() {
        return SELENIUM_MAXTIMEOUT;
    }

    public static int getMinTimeout() {
        return SELENIUM_MINTIMEOUT;
    }

    private static String loadAndGetResourceLocation(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(fileName).toString();
    }

    public String getCssLocatorForFluent(FluentWebElement element) {

        String val = element.getElement().toString();
        val = val.substring(val.toLowerCase().lastIndexOf("selector:") + 10, val.length() - 1).trim();

        return val;

    }

    public void selectByVisibleText(String text, FluentWebElement selectElement) {

        Select dropdown = new Select(selectElement.getElement());
        dropdown.selectByVisibleText(text);
    }

    public void selectByValue(String value, FluentWebElement selectElement) {
        Select dropdown = new Select(selectElement.getElement());
        dropdown.selectByValue(value);
    }

    public void selectDropDownValue(FluentList<FluentWebElement> dropDownList, String searchValue) {
        for (FluentWebElement value : dropDownList) {
            //System.out.println("value is "+value.getText());

            if (value.getText().trim().equalsIgnoreCase(searchValue)) {
                value.click();
                break;
            }

        }
    }


    public Fluent click(FluentDefaultActions fluentObject) {
        FluentWebElement element = (FluentWebElement) fluentObject;
        awaitForElementPresence(element);
        try {
            return super.click(fluentObject);
        } catch (WebDriverException e) {
            e.printStackTrace();
        }

        return this;

    }

    public void selectHighlitedValueInDropDown() {
        Assert.assertTrue(highlightedSearchResults.size() > 0, "No Search Results are coming for the given Input");
        highlightedSearchResults.get(0).click();
    }

    public void selectValueBYMatchingText(String value) {
        for (FluentWebElement e : find(".RCB-align-left .RCB-list-item")) {
            if (e.getText().trim().equalsIgnoreCase(value)) {
                e.click();
                break;
            }
        }
    }

    public String getRandomName() {
        Faker faker = new Faker();

        return faker.name().firstName();

    }

    public Boolean awaitForElementPresence(final FluentWebElement element) {

        Function<Fluent, FluentWebElement> function = new Function<Fluent, FluentWebElement>() {
            public FluentWebElement apply(Fluent fluent) {
                if (element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        };
        try {
            await().atMost(getMaxTimeout()).until(function);
        } catch (WebDriverException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Boolean awaitForPageToLoad() {
        try {
            await().atMost(getMaxTimeout()).withMessage("Waiting for the Page Load is failed").untilPage().isLoaded();
            return true;
        } catch (Exception e) {
            System.out.println("Waiting for the Page Load is failed");
            return false;
        }
    }

    public void switchTabTo(int tabnumber)
    {
        ArrayList<String> tabs = new ArrayList<String> (getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(tabnumber));
    }

    public Boolean awaitForElementNotDisplayed(final FluentWebElement element) {
        Function<Fluent, FluentWebElement> isNotDisplayedFunction = new Function<Fluent, FluentWebElement>() {
            public FluentWebElement apply(Fluent fluent) {
                if (!element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        };
        try {
            await().atMost(getMaxTimeout()).until(isNotDisplayedFunction);
            return true;
        } catch (WebDriverException e) {
            System.out.println("awaitForElementNotDisplayed is failed for element . Exception is : " + e.getMessage());
            return false;
        }
    }


    public void threadWait() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Boolean awaitTillElementDisplayed(FluentWebElement element) {
        int count = 0;
        while (count < 12) {
            try {
                if (!element.isDisplayed())
                    try {
                        threadWait();
                        count++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;

                    }
                else
                    return true;
            } catch (WebDriverException e) {
                return false;
            }
        }
        System.out.println("awaitTillElementDisplayed is failed for the element ");
        return false;
    }


    public Boolean awaitTillElementHasText(FluentWebElement element, String text) {
        try {
            await().atMost(getMaxTimeout()).ignoring(RuntimeException.class).until(getCssLocatorForFluent(element)).containsText(text);
            return true;
        } catch (Exception e) {
            System.out.println("awaitTillElementHasText is failed for elment ");
            return false;
        }
    }

    public boolean checkElementPresence(FluentWebElement element) {
        try {

            if (element.isDisplayed())
                return true;
            else
                return false;
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    public void unbxdInputBoxSearch(FluentWebElement element, String name) {


        try {
            awaitForElementPresence(element);
            element.getElement().sendKeys(name);
            threadWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleAlert(String action) {

        org.openqa.selenium.Alert alert = getDriver().switchTo().alert();
        switch (action) {

            case "accept":
                alert.accept();
                break;
            case "dismiss":
                alert.dismiss();
                break;
            default:
                return;
        }


    }

    public void waitForLoaderToDisAppear(By locator, String name) {
        String result = null;
        WebDriverWait wait = new WebDriverWait(getDriver(), 300);
        // FluentWebElement element;
        try {
            APPLICATION_LOGS.debug("waiting for " + name + " to disappear");
            System.out.println("waiting for " + name + " to disappear");

            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));


            APPLICATION_LOGS.debug("waited for " + name + " to disappear");
            System.out.println("waited for" + name + " to disappear");

        } catch (Throwable ElementNotFoundException) {

            APPLICATION_LOGS
                    .debug("Exception came while waiting for " + name + " to load");
            System.err
                    .println("Exception came while waiting for " + name + " to load");


        }
    }

    public void waitForLoaderToDisAppear(By locator, String name, int numOfRetries, int interval) {


        //String result = null;
        WebDriverWait wait = new WebDriverWait(getDriver(), 180);
        // FluentWebElement element;
        try {
            APPLICATION_LOGS.debug("waiting for " + name + " to disappear");
            System.out.println("waiting for " + name + " to disappear");

            boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            if (result) {
                return;
            }

            APPLICATION_LOGS.debug("waited for " + name + " to disappear");
            System.out.println("waited for" + name + " to disappear");

        } catch (Throwable ElementNotFoundException) {

            APPLICATION_LOGS
                    .debug("Exception came while waiting for " + name + " to load");
            System.err
                    .println("Exception came while waiting for " + name + " to load");


        }
    }

    public void waitForElementAppear(By locator, String name, int numOfRetries, int interval) {
        //String result = null;
        WebDriverWait wait = new WebDriverWait(getDriver(), interval);
        // FluentWebElement element;
        for (int i = 0; i < numOfRetries; i++) {
            try {
                APPLICATION_LOGS.debug("waiting for " + name + " to appear ");
                System.out.println("waiting for " + name + " to appear " + i + " round ");

                WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (result.isDisplayed()) {
                    return;
                }

                APPLICATION_LOGS.debug("waited for " + name + " to appear");
                System.out.println("waited for " + name + " to appear");


            } catch (Exception e) {

                APPLICATION_LOGS
                        .debug("Exception came while waiting for " + name + " to load");
                System.err
                        .println("Exception came while waiting for " + name + " to load");


            }
        }
    }

    public void waitForElementToAppear(By locator, String name) {
        //String result = null;
        WebDriverWait wait = new WebDriverWait(getDriver(), 180);
        // FluentWebElement element;
        try {
            APPLICATION_LOGS.debug("waiting for " + name + " to appear");
            System.out.println("waiting for " + name + " to appear");

            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            if (result.isDisplayed()) {
                return;
            }

            APPLICATION_LOGS.debug("waited for " + name + " to appear");
            System.out.println("waited for" + name + " to appear");

        } catch (Throwable ElementNotFoundException) {

            APPLICATION_LOGS
                    .debug("Exception came while waiting for " + name + " to load");
            System.err
                    .println("Exception came while waiting for " + name + " to load");
        }
    }



    public static void awaitTillFrameToBeAvailable(FluentWebElement frame)
    {
        ExpectedConditions.frameToBeAvailableAndSwitchToIt((WebElement) frame);
    }

    public  void waitForElementToBeClickable(FluentWebElement locator, String elemName) {

        String result = null;
        WebDriverWait wait = new WebDriverWait(getDriver(), 15);
        // FluentWebElement element;
        try {
            APPLICATION_LOGS.debug("waiting for " + elemName + "to appear");
            System.out.println("waiting for " + elemName + "to appear");

            if(locator.isEnabled())
                wait.until(ExpectedConditions.elementToBeClickable(locator.getElement()));

            APPLICATION_LOGS.debug("waited for " + elemName + "to appear");
            System.out.println("waited for " + elemName + "to appear");

        } catch (Throwable ElementNotFoundException) {

            APPLICATION_LOGS
                    .debug("Exception came while waiting for element to load");
            System.err
                    .println("Exception came while waiting for element to load");


        }
    }

    /**
     * public static ExpectedCondition<FluentWebElement>
     * visibilityOfElementLocated(final FluentWebElement locator) method specification :-
     *
     * 1) Waits for the web element to appear on the page 2) FluentWebElement
     * toReturn.isDisplayed() -> Returns true if displayed on the page, else
     * returns false
     *
     * @param : Locator to locate the web element
     * @return : ExpectedCondition about the web element
     */

    public static ExpectedCondition<FluentWebElement> visibilityOfElementLocated(final FluentWebElement locator) {

        return new ExpectedCondition<FluentWebElement>() {

            public FluentWebElement apply(WebDriver driver) {

                // Store the web element
                FluentWebElement toReturn = locator;

                // Check whether the web element is displayed on the page
                if (toReturn.isDisplayed() && toReturn.isEnabled())
                    return toReturn;

                return null;

            }

        };

    }

    /**
     * public static void waitForElementToLoad(FluentWebElement locator) method specification
     * :-
     *
     * 1) Waits for the web element to appear on the page 2) new
     * WebDriverWait(BrowserInitializer.getDriver(), 60) -> Waits for 60 seconds 3)
     * wait.until((ExpectedCondition<Boolean>) -> Wait until expected condition
     * (All documents present on the page get ready) met
     *
     * @param : no parameters passed
     * @return : void
     */

    public  void waitForElementToLoad(final FluentWebElement locator, String eleName) {

        APPLICATION_LOGS.debug("Waiting for "+eleName+" to load on the page");
        System.out.println("Waiting for "+eleName+" to load on the page");

        try {
            if(locator.isEnabled()){
                // Waits for 60 seconds
                Wait<WebDriver> wait = new WebDriverWait(getDriver(), 60);
                // Wait until the element is located on the page
                @SuppressWarnings("unused")

                FluentWebElement element = wait
                        .until(visibilityOfElementLocated(locator));
            }

            // Log result
            APPLICATION_LOGS
                    .debug("Waiting ends ... "+eleName +" loaded on the page");
            System.out
                    .println("Waiting ends ... "+eleName +" loaded on the page");

        }
        catch (StaleElementReferenceException staleException) {
            System.out.println("Caught Stale element exception for " +eleName + "+...Retrying...");
            Wait<WebDriver> wait = new WebDriverWait(getDriver(), 30);
            wait.until(visibilityOfElementLocated(locator));
        }
        catch (Throwable waitForElementException) {

            // Log error
            APPLICATION_LOGS
                    .debug("Error came while waiting for "+eleName + " to appear : "
                            + waitForElementException.getMessage());
            System.err
                    .println("Error came while waiting for " +eleName + " to appear : "
                            + waitForElementException.getMessage());

        }

    }

    /**
     * public static String retrieveText(FluentWebElement locator,String elemName) method
     * specification :-
     *
     * 1) Return retrieved text from webpage 2)
     * locator.getText() -> Retrieves text from the web
     * element targeted by specified locator
     *
     * @param : Locator for the web element, Name of the web element
     * @return : Text retrieved from the webpage
     */

    public  String retrieveText(FluentWebElement locator, String elemName) {

        String retrievedText = null;

        APPLICATION_LOGS.debug("Retrieving Text from : " + elemName);
        System.out.println("Retrieving Text from : " + elemName);

        try {
            // Wait for web element to load on the page
            waitForElementToBeClickable(locator, elemName);

            // Highlight the web element
            highlightElement(locator);

            if(locator.isEnabled() && locator.isDisplayed()){
                // Retrieve text from web element
                retrievedText = locator.getText().trim();
            }
            else {
                ThreadWait();
                retrievedText = locator.getText().trim();
            }

            // Log result
            APPLICATION_LOGS.debug("Retrieved text : " + retrievedText);
            System.out.println("Retrieved text : " + retrievedText);

        }
        catch (StaleElementReferenceException getTextStaleException) {

            System.err.println("Stale Element Exception is handled for '" + elemName
                    + "' : " + getTextStaleException.getMessage());
            ThreadWait();

            // Wait for link to appear on the page
            waitForElementToLoad(locator, elemName);

            // Click on the link
            retrievedText = locator.getText().trim();

            // Log result
            System.out.println("Retrying...Clicked on : " + elemName);
            APPLICATION_LOGS.debug("Retrying...Clicked on : " + elemName);

        }
        catch (Throwable retrieveTextException) {

            // Log error
            System.err.println("Error while Getting Text from '" + elemName
                    + "' : " + retrieveTextException.getMessage());
            APPLICATION_LOGS.debug("Error while Getting Text from '" + elemName
                    + "' : " + retrieveTextException.getMessage());

        }
        return retrievedText;
    }

    public static void ThreadWait() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * public static void highlightElement(WebDriver BrowserInitializer.getDriver(), By Locator) method
     * specification
     *
     * This method is used to highlight the element of choice
     *
     * @param : BrowserInitializer.getDriver()
     * @param : (Locator) locator of the element to highlight
     */
    public  void highlightElement(FluentWebElement Locator) {

        try {

            for (int i = 0; i < 3; i++) {
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                // js.executeScript("arguments[0].setAttribute('style', arguments[1]);",BrowserInitializer.getDriver().findElement(Locator),
                // "color: red; border: 2px solid red;");
                js.executeScript(
                        "arguments[0].setAttribute('style', arguments[1]);",
                        Locator.getElement(),
                        "background-color: yellow; outline: 1px solid rgb(136, 255, 136);");

            }
        }
        catch (Throwable t) {
            System.out.println("Error came : " + t.getMessage());
            APPLICATION_LOGS.debug("Error came : " + t.getMessage());
        }

    }

    public void scrollToElement(FluentWebElement locator, String name) {

        APPLICATION_LOGS.debug("Scrolling to "+name);
        System.out.println("Scrolling to "+name);

        // Initialize Javascript executor
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        try {

            js.executeScript("arguments[0].scrollIntoView(true);", locator.getElement());
            System.out.println("Scrolled to "+name);
            APPLICATION_LOGS.debug("Scrolled to "+name);

        }

        catch (Throwable scrollException) {

            // Log error
            System.err.println("Error while scrolling "+name);
            APPLICATION_LOGS.debug("Error while scrolling "+name);

        }
    }

    public void scrollToBottom() {

        APPLICATION_LOGS.debug("Scrolling to bottom of page");
        System.out.println("Scrolling to bottom of page");

        try {
            ((JavascriptExecutor) getDriver())
                    .executeScript("window.scrollTo(0, document.body.scrollHeight)");

            System.out.println("Scrolled to bottom of page");
            APPLICATION_LOGS.debug("Scrolled to bottom of page");

        }

        catch (Throwable scrollException) {

            // Log error
            System.err.println("Error while scrolling down to bottom of the page");
            APPLICATION_LOGS.debug("Error while scrolling down to bottom of the page");

        }
    }

    public
    void scrollToTop() {

        APPLICATION_LOGS.debug("Scrolling to top of page");
        System.out.println("Scrolling to top of page");

        try {
            ((JavascriptExecutor) getDriver())
                    .executeScript("window.scrollTo(0, 0)");

            System.out.println("Scrolled to top of page");
            APPLICATION_LOGS.debug("Scrolled to top of page");

        }

        catch (Throwable scrollException) {

            // Log error
            System.err.println("Error while scrolling to top of the page");
            APPLICATION_LOGS.debug("Error while scrolling to top of the page");

        }
    }

    public boolean checkModalWindow()
    {
        return findFirst("body").getAttribute("class").contains("modal-open");
    }


}
