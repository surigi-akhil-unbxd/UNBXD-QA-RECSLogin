package UnbxdTests.testNG.ui;

import lib.BrowserInitializer;
import lib.EnvironmentConfig;
import org.fluentlenium.core.FluentAdapter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import util.ExtentManager;

public class BaseTest extends FluentAdapter {

    public  WebDriver driver=null;
    public final String testDataPath="src/test/resources/testData/";
    protected static ExtentReports extent;
    protected ExtentTest test;

    public void setUp() {
        System.out.println("Starting setUp in BaseTest...");
        try {
            // Initialize ExtentReports
            extent = ExtentManager.getInstance();
            System.out.println("Creating BrowserInitializer...");
            BrowserInitializer initializer = new BrowserInitializer();
            
            System.out.println("Initializing browser...");
            initializer.init();
            
            System.out.println("Getting driver from initializer...");
            driver = initializer.getDriver();
            System.out.println("Driver status after initialization: " + (driver != null ? "Driver exists" : "Driver is null"));
            
            if (driver != null) {
                System.out.println("Initializing FluentLenium adapter...");
                initFluent(driver).withDefaultUrl(EnvironmentConfig.getLoginUrl());
                System.out.println("FluentLenium initialization completed");
                
                System.out.println("Initializing test...");
                initTest();
                System.out.println("Test initialization completed");
            } else {
                System.out.println("WARNING: Driver is null after initialization!");
            }
        }
        catch(Exception e)
        {
            System.out.println("Browser Initialization is failed with Exception "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void openNewTab()
    {
        ((JavascriptExecutor) driver).executeScript("window.open()");
    }

   @AfterClass(alwaysRun = true)
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
            // Flush ExtentReports
            if (extent != null) {
                extent.flush();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

   @AfterClass(alwaysRun = true)
    public void removeContextForTest() {
        EnvironmentConfig.unSetContext();
    }
}
