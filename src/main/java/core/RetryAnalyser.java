package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RetryAnalyser implements IRetryAnalyzer {
    
    private static final Logger logger = Logger.getLogger(RetryAnalyser.class.getName());
    
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = RetryConfig.getMaxRetryCount();
        
        if (!result.isSuccess()) {
            if (retryCount < maxRetryCount) {
                retryCount++;
                logger.log(Level.INFO, "Test failed, retrying... Attempt " + retryCount + " of " + maxRetryCount);
                logger.log(Level.INFO, "Test: " + result.getMethod().getMethodName() + 
                           " | Class: " + result.getTestClass().getName());
                
                // Log the exception if available
                if (result.getThrowable() != null) {
                    logger.log(Level.WARNING, "Failure reason: " + result.getThrowable().getMessage());
                }
                
                // Add delay between retries
                try {
                    Thread.sleep(RetryConfig.getRetryDelayMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.log(Level.WARNING, "Retry delay interrupted", e);
                }
                
                return true; // Retry the test
            } else {
                logger.log(Level.SEVERE, "Test failed after " + maxRetryCount + " retries. Marking as failed.");
                result.setStatus(ITestResult.FAILURE);
            }
        } else {
            if (retryCount > 0) {
                logger.log(Level.INFO, "Test passed on retry attempt " + retryCount + 
                           " | Test: " + result.getMethod().getMethodName());
            }
            result.setStatus(ITestResult.SUCCESS);
        }
        return false; // Don't retry anymore
    }
    
    /**
     * Get the current retry count for debugging purposes
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Reset retry count (useful for test cleanup)
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
}
