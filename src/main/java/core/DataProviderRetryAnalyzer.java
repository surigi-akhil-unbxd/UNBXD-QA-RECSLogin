package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.DataProvider;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Specialized retry analyzer for data provider tests
 * Retries only the failed data sets, not the entire test method
 */
public class DataProviderRetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = Logger.getLogger(DataProviderRetryAnalyzer.class.getName());
    
    // Track retry attempts per data set (test method + data index)
    private static final Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();
    
    @Override
    public boolean retry(ITestResult result) {
        // Check if this is a data provider test
        if (!isDataProviderTest(result)) {
            logger.log(Level.FINE, "Not a data provider test, using standard retry logic");
            return false;
        }
        
        String dataSetKey = getDataSetKey(result);
        int maxRetryCount = RetryConfig.getMaxRetryCount();
        int currentRetryCount = retryCountMap.getOrDefault(dataSetKey, 0);
        
        if (!result.isSuccess()) {
            if (currentRetryCount < maxRetryCount) {
                currentRetryCount++;
                retryCountMap.put(dataSetKey, currentRetryCount);
                
                logger.log(Level.INFO, "Data set failed, retrying... Attempt " + currentRetryCount + " of " + maxRetryCount);
                logger.log(Level.INFO, "Test: " + result.getMethod().getMethodName() + 
                           " | Class: " + result.getTestClass().getName() +
                           " | Data Index: " + getDataIndex(result));
                
                // Log the data set parameters
                logDataSetParameters(result);
                
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
                
                return true; // Retry this specific data set
            } else {
                logger.log(Level.SEVERE, "Data set failed after " + maxRetryCount + " retries. Marking as failed.");
                logger.log(Level.SEVERE, "Failed data set: " + dataSetKey);
                result.setStatus(ITestResult.FAILURE);
            }
        } else {
            if (currentRetryCount > 0) {
                logger.log(Level.INFO, "Data set passed on retry attempt " + currentRetryCount + 
                           " | Test: " + result.getMethod().getMethodName() +
                           " | Data Index: " + getDataIndex(result));
            }
            result.setStatus(ITestResult.SUCCESS);
        }
        
        return false; // Don't retry anymore
    }
    
    /**
     * Check if the test method uses a data provider
     */
    private boolean isDataProviderTest(ITestResult result) {
        try {
            // Check if the method has parameters (indicates data provider usage)
            Object[] parameters = result.getParameters();
            return parameters != null && parameters.length > 0;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error checking if test is data provider test", e);
            return false;
        }
    }
    
    /**
     * Generate a unique key for the data set (method + data index)
     */
    private String getDataSetKey(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        int dataIndex = getDataIndex(result);
        return className + "." + methodName + "_" + dataIndex;
    }
    
    /**
     * Get the data index from the test result
     */
    private int getDataIndex(ITestResult result) {
        try {
            Object[] parameters = result.getParameters();
            if (parameters != null && parameters.length > 0) {
                // For data provider tests, the last parameter often contains the data index
                // or we can use the test result's instance hash
                return result.getInstance().hashCode();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error getting data index", e);
        }
        return 0;
    }
    
    /**
     * Log the data set parameters for debugging
     */
    private void logDataSetParameters(ITestResult result) {
        try {
            Object[] parameters = result.getParameters();
            if (parameters != null && parameters.length > 0) {
                StringBuilder paramLog = new StringBuilder("Data set parameters: ");
                for (int i = 0; i < parameters.length; i++) {
                    paramLog.append("param[").append(i).append("]=").append(parameters[i]);
                    if (i < parameters.length - 1) {
                        paramLog.append(", ");
                    }
                }
                logger.log(Level.FINE, paramLog.toString());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error logging data set parameters", e);
        }
    }
    
    /**
     * Clear retry count for a specific data set (useful for cleanup)
     */
    public static void clearRetryCount(String dataSetKey) {
        retryCountMap.remove(dataSetKey);
    }
    
    /**
     * Clear all retry counts (useful for test suite cleanup)
     */
    public static void clearAllRetryCounts() {
        retryCountMap.clear();
        logger.log(Level.INFO, "Cleared all retry counts");
    }
    
    /**
     * Get current retry count for a data set
     */
    public static int getRetryCount(String dataSetKey) {
        return retryCountMap.getOrDefault(dataSetKey, 0);
    }
    
    /**
     * Get all retry statistics
     */
    public static Map<String, Integer> getRetryStatistics() {
        return new ConcurrentHashMap<>(retryCountMap);
    }
} 