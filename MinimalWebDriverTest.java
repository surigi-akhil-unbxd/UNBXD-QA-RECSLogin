import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MinimalWebDriverTest {
    public static void main(String[] args) {
        try {
            // Set path to chromedriver if needed
            // System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
            WebDriver driver = new ChromeDriver();
            driver.get("https://www.google.com");
            System.out.println("Title: " + driver.getTitle());
            driver.quit();
            System.out.println("WebDriver test completed successfully.");
        } catch (Exception e) {
            System.out.println("WebDriver test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 