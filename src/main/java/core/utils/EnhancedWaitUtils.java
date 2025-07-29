package core.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced wait utilities for better element synchronization and handling UI changes.
 * Provides fallback locators and improved wait strategies for dynamic elements.
 */
public class EnhancedWaitUtils {
    
    /**
     * Wait for element to be present and clickable with multiple fallback locators
     */
    public static WebElement waitForElementWithFallback(WebDriver driver, By primaryLocator, By... fallbackLocators) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        // Try primary locator first
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(primaryLocator));
        } catch (Exception e) {
            System.out.println("[DEBUG] Primary locator failed: " + primaryLocator);
        }
        
        // Try fallback locators
        for (By fallbackLocator : fallbackLocators) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(fallbackLocator));
                System.out.println("[DEBUG] Found element using fallback locator: " + fallbackLocator);
                return element;
            } catch (Exception e) {
                System.out.println("[DEBUG] Fallback locator failed: " + fallbackLocator);
            }
        }
        
        throw new NoSuchElementException("Element not found with any available locator");
    }
    
    /**
     * Wait for element to be stable (not changing value/state)
     */
    public static void waitForElementStability(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        String initialValue = element.getAttribute("value");
        
        try {
            wait.until((ExpectedCondition<Boolean>) d -> {
                String currentValue = element.getAttribute("value");
                return currentValue.equals(initialValue);
            });
        } catch (Exception e) {
            System.out.println("[WARN] Element stability wait timed out");
        }
    }
    
    /**
     * Wait for loading indicators to disappear
     */
    public static void waitForLoadingToComplete(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        try {
            List<WebElement> loadingElements = driver.findElements(By.cssSelector(
                ".loading, .spinner, .loader, [class*='loading'], [class*='spinner'], .progress"
            ));
            
            if (!loadingElements.isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOfAllElements(loadingElements));
                System.out.println("[DEBUG] Loading indicators disappeared");
            }
        } catch (Exception e) {
            System.out.println("[WARN] Loading wait timed out or no loading indicators found");
        }
    }
    
    /**
     * Wait for dropdown options to load with multiple selectors
     */
    public static void waitForDropdownOptions(WebDriver driver, String... selectors) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        
        // Wait for loading to complete first
        waitForLoadingToComplete(driver);
        
        // Try multiple selectors for dropdown options
        boolean optionsLoaded = false;
        for (String selector : selectors) {
            try {
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selector)));
                optionsLoaded = true;
                System.out.println("[DEBUG] Dropdown options loaded using selector: " + selector);
                break;
            } catch (Exception ignored) {}
        }
        
        if (!optionsLoaded) {
            System.out.println("[WARN] Dropdown options did not load within timeout");
        }
    }
    
    /**
     * Handle stale element reference by re-finding the element
     */
    public static WebElement handleStaleElement(WebDriver driver, WebElement element, By locator) {
        try {
            // Try to interact with the element
            element.isDisplayed();
            return element;
        } catch (StaleElementReferenceException e) {
            // Element is stale, re-find it
            System.out.println("[DEBUG] Element is stale, re-finding...");
            WebDriverWait wait = new WebDriverWait(driver, 5);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        }
    }
    
    /**
     * Click element with multiple strategies (normal click, JavaScript click, Actions)
     */
    public static void clickElementWithFallback(WebDriver driver, WebElement element) {
        try {
            // Strategy 1: Normal click
            element.click();
            System.out.println("[DEBUG] Element clicked normally");
        } catch (Exception e) {
            System.out.println("[WARN] Normal click failed, trying JavaScript click");
            try {
                // Strategy 2: JavaScript click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("[DEBUG] Element clicked using JavaScript");
            } catch (Exception jsEx) {
                System.out.println("[WARN] JavaScript click failed, trying Actions");
                try {
                    // Strategy 3: Actions click
                    new Actions(driver).moveToElement(element).click().perform();
                    System.out.println("[DEBUG] Element clicked using Actions");
                } catch (Exception actionsEx) {
                    throw new RuntimeException("All click strategies failed", actionsEx);
                }
            }
        }
    }
    
    /**
     * Clear input field with multiple strategies
     */
    public static void clearInputWithFallback(WebDriver driver, WebElement element) {
        try {
            // Strategy 1: Normal clear
            element.clear();
        } catch (Exception e) {
            System.out.println("[WARN] Normal clear failed, trying JavaScript clear");
            try {
                // Strategy 2: JavaScript clear
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
            } catch (Exception jsEx) {
                System.out.println("[WARN] JavaScript clear failed, trying keyboard shortcuts");
                try {
                    // Strategy 3: Keyboard shortcuts
                    element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                    element.sendKeys(Keys.DELETE);
                } catch (Exception kbEx) {
                    System.out.println("[WARN] Keyboard clear failed, trying backspace");
                    try {
                        // Strategy 4: Multiple backspaces
                        element.sendKeys(Keys.END);
                        for (int i = 0; i < 50; i++) {
                            element.sendKeys(Keys.BACK_SPACE);
                        }
                    } catch (Exception bsEx) {
                        throw new RuntimeException("All clear strategies failed", bsEx);
                    }
                }
            }
        }
    }
    
    /**
     * Wait for page to be fully loaded
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        
        try {
            wait.until((ExpectedCondition<Boolean>) d -> {
                String readyState = ((JavascriptExecutor) d).executeScript("return document.readyState").toString();
                return "complete".equals(readyState);
            });
        } catch (Exception e) {
            System.out.println("[WARN] Page load wait timed out");
        }
    }
    
    /**
     * Wait for AJAX requests to complete
     */
    public static void waitForAjaxToComplete(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        try {
            wait.until((ExpectedCondition<Boolean>) d -> {
                Long activeRequests = (Long) ((JavascriptExecutor) d).executeScript(
                    "return window.jQuery ? jQuery.active : 0"
                );
                return activeRequests == 0;
            });
        } catch (Exception e) {
            System.out.println("[WARN] AJAX wait timed out or jQuery not available");
        }
    }
    
    /**
     * Wait for element to be visible and not covered by overlays
     */
    public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            
            // Check if element is not covered by overlays
            wait.until((ExpectedCondition<Boolean>) d -> {
                try {
                    Point location = element.getLocation();
                    Dimension size = element.getSize();
                    
                    // Check if element is in viewport
                    return location.getX() >= 0 && location.getY() >= 0 &&
                           location.getX() + size.getWidth() <= driver.manage().window().getSize().getWidth() &&
                           location.getY() + size.getHeight() <= driver.manage().window().getSize().getHeight();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            System.out.println("[WARN] Element visibility wait timed out");
        }
    }
    
    /**
     * Retry operation with exponential backoff
     */
    public static <T> T retryOperation(WebDriver driver, java.util.function.Supplier<T> operation, int maxRetries) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                System.out.println("[DEBUG] Operation failed on attempt " + attempt + ": " + e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        // Exponential backoff: 1s, 2s, 4s, 8s...
                        Thread.sleep((long) Math.pow(2, attempt - 1) * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
    }
} 